package com.smart.heart.es6.example.json.query.template;

import com.smart.heart.es6.example.json.constant.Constant;
import com.smart.heart.es6.example.json.query.bean.ESBoolQueryBean;
import com.smart.heart.es6.example.json.query.bean.ESQueryBean;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.net.UnknownHostException;
import java.util.Map;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/3/11
 */
public class ESBoolQueryTemplate extends ESQueryTemplate {


    public ESBoolQueryTemplate() throws UnknownHostException {
    }

    @Override
    protected void boolSearch(ESQueryBean esQueryBean, SearchSourceBuilder sourceBuilder) {
        //FIXME：注：未针对嵌套Bool做处理 Arnold.zhao 2021/3/3
        //针对ESBoolQueryBean 处理;
        ESBoolQueryBean esBoolQueryBean = (ESBoolQueryBean) esQueryBean;
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if (esBoolQueryBean.getBoolMustMap() != null && !esBoolQueryBean.getBoolMustMap().isEmpty()) {
            each(esBoolQueryBean.getBoolMustMap(), boolQueryBuilder, Constant.MUST);
        }
        if (esBoolQueryBean.getBoolFilterMap() != null && !esBoolQueryBean.getBoolFilterMap().isEmpty()) {
            each(esBoolQueryBean.getBoolMustMap(), boolQueryBuilder, Constant.FILTER);
        }
        if (esBoolQueryBean.getBoolNotMustMap() != null && !esBoolQueryBean.getBoolNotMustMap().isEmpty()) {
            each(esBoolQueryBean.getBoolMustMap(), boolQueryBuilder, Constant.NOT_MUST);
        }
        if (esBoolQueryBean.getBoolShouldMap() != null && !esBoolQueryBean.getBoolShouldMap().isEmpty()) {
            each(esBoolQueryBean.getBoolMustMap(), boolQueryBuilder, Constant.SHOULD);
        }
        sourceBuilder.query(boolQueryBuilder);
    }

    private void each(Map<String, ESBoolQueryBean.ConditionBean> map, BoolQueryBuilder boolQueryBuilder, String flag) {
        map.forEach((conditionType, conditionBean) -> {
            each(flag, boolQueryBuilder, conditionType, conditionBean);
        });
    }

    private void each(String flag, BoolQueryBuilder boolQueryBuilder, String conditionType, ESBoolQueryBean.ConditionBean conditionBean) {
        if (flag.equals(Constant.MUST)) {
            if (conditionType.equals(Constant.ConditionType.MATCH.name())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery(conditionBean.getKey(), conditionBean.getValue()));
            }
            if (conditionType.equals(Constant.ConditionType.TERM.name())) {
                boolQueryBuilder.must(QueryBuilders.termQuery(conditionBean.getKey(), conditionBean.getValue()));
            }
            if (conditionType.equals(Constant.ConditionType.RANGE.name())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery(conditionBean.getKey()).gt(conditionBean.getGte()).lt(conditionBean.getLte()));
            }
        } else if (flag.equals(Constant.FILTER)) {
            if (conditionType.equals(Constant.ConditionType.MATCH.name())) {
                boolQueryBuilder.filter(QueryBuilders.matchQuery(conditionBean.getKey(), conditionBean.getValue()));
            }
            if (conditionType.equals(Constant.ConditionType.TERM.name())) {
                boolQueryBuilder.filter(QueryBuilders.termQuery(conditionBean.getKey(), conditionBean.getValue()));
            }
            if (conditionType.equals(Constant.ConditionType.RANGE.name())) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(conditionBean.getKey()).gt(conditionBean.getGte()).lt(conditionBean.getLte()));
            }
        } else if (flag.equals(Constant.SHOULD)) {
            if (conditionType.equals(Constant.ConditionType.MATCH.name())) {
                boolQueryBuilder.should(QueryBuilders.matchQuery(conditionBean.getKey(), conditionBean.getValue()));
            }
            if (conditionType.equals(Constant.ConditionType.TERM.name())) {
                boolQueryBuilder.should(QueryBuilders.termQuery(conditionBean.getKey(), conditionBean.getValue()));
            }
            if (conditionType.equals(Constant.ConditionType.RANGE.name())) {
                boolQueryBuilder.should(QueryBuilders.rangeQuery(conditionBean.getKey()).gt(conditionBean.getGte()).lt(conditionBean.getLte()));
            }
        } else if (flag.equals(Constant.NOT_MUST)) {
            if (conditionType.equals(Constant.ConditionType.MATCH.name())) {
                boolQueryBuilder.mustNot(QueryBuilders.matchQuery(conditionBean.getKey(), conditionBean.getValue()));
            }
            if (conditionType.equals(Constant.ConditionType.TERM.name())) {
                boolQueryBuilder.mustNot(QueryBuilders.termQuery(conditionBean.getKey(), conditionBean.getValue()));
            }
            if (conditionType.equals(Constant.ConditionType.RANGE.name())) {
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(conditionBean.getKey());
                if (conditionBean.getGte() != null) {
                    rangeQueryBuilder.gte(conditionBean.getGte());
                }
                if (conditionBean.getGt() != null) {
                    rangeQueryBuilder.gt(conditionBean.getGt());
                }
                if (conditionBean.getLte() != null) {
                    rangeQueryBuilder.lte(conditionBean.getLte());
                }
                if (conditionBean.getLt() != null) {
                    rangeQueryBuilder.lt(conditionBean.getLt());
                }
                boolQueryBuilder.mustNot(rangeQueryBuilder);
            }
        }

    }
}
