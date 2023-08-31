package com.smart.heart.es6.example.json.query.template;

import com.smart.heart.es6.example.json.query.bean.ESQueryBean;
import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/3/11
 */
public class ESQueryTemplate {
    private RestHighLevelClient restHighLevelClient;

    Logger log = LoggerFactory.getLogger(ESQueryTemplate.class);

    public ESQueryTemplate() throws UnknownHostException {
        restHighLevelClient = new ES6Template(ES6Connection.ESClientMode.REST).getRestHighLevelClient();
    }

    public static void main(String[] args) {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("").lt(20).gt(20));

        searchRequest.source(searchSourceBuilder);

        System.out.println(searchRequest.toString());
    }

    public List<Map<String, Object>> search(ESQueryBean queryBean) {
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            //设置查询超时
            if (queryBean.getTimeOut() != null) {
                sourceBuilder.timeout(new TimeValue(queryBean.getTimeOut(), TimeUnit.SECONDS));
            } else {
                sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
            }

            //查询条件
            if (queryBean.getQueryMap() != null && (!queryBean.getQueryMap().isEmpty())) {
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                if (queryBean.getQueryType().equals("1")) {
                    queryBean.getQueryMap().forEach((k, v) -> {
                        boolQueryBuilder.must(QueryBuilders.termQuery(k, v));
                    });
                } else {
                    queryBean.getQueryMap().forEach((k, v) -> {
                        boolQueryBuilder.must(QueryBuilders.wildcardQuery(k, v.toString()));
                    });
                }
                sourceBuilder.query(boolQueryBuilder);
            }


            //排序
            if (queryBean.getSort() != null && (!queryBean.getSort().isEmpty())) {
                queryBean.getSort().forEach((k, v) -> {
                    sourceBuilder.sort(new FieldSortBuilder(k).order(v ? SortOrder.ASC : SortOrder.DESC));
                });
            } else {
                sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
            }

            //设置页数 页大小
            if (queryBean.getPageNum() != null && queryBean.getSize() != null) {
                sourceBuilder.from(queryBean.getPageNum()).size(queryBean.getSize());
            } else {
                sourceBuilder.from(0).size(20);
            }

            //返回和排除列
            String[] includes = CollectionUtils.isEmpty(queryBean.getIncludeFields()) ? null : queryBean.getIncludeFields();
            String[] excludeFields = CollectionUtils.isEmpty(queryBean.getExcludeFields()) ? null : queryBean.getExcludeFields();
            sourceBuilder.fetchSource(includes, excludeFields);

            //增加子类扩展入口
            boolSearch(queryBean, sourceBuilder);

            //组装查询请求
            SearchRequest searchRequest = new SearchRequest();
            //索引
            if (!StringUtils.isEmpty(queryBean.getIndex())) {
                searchRequest.indices(queryBean.getIndex());
            }
            //组合条件
            searchRequest.source(sourceBuilder);
            //请求体输出
            System.out.println(searchRequest.source().toString());
            SearchResponse rp = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            //解析返回
            if (rp.status() != RestStatus.OK) {
                return Collections.emptyList();
            }
            //获取source
            return Arrays.stream(rp.getHits().getHits()).map(b -> {
                return b.getSourceAsMap();
            }).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("查询ES出现异常：{}|{}", e.getMessage(), queryBean);
            return null;
        }
    }

    /**
     * 供子类重载
     * Arnold.zhao 2021/3/4
     *
     * @param sourceBuilder
     */
    protected void boolSearch(ESQueryBean esQueryBean, SearchSourceBuilder sourceBuilder) {

    }
}
