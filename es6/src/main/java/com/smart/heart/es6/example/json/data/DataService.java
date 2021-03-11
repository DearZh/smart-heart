package com.smart.heart.es6.example.json.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.heart.es6.example.json.constant.Constant;
import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/3/11
 */
public class DataService {

    private final static Logger log = LoggerFactory.getLogger(DataService.class);

    private static RestHighLevelClient restHighLevelClient;

    public DataService() throws UnknownHostException {
        restHighLevelClient = new ES6Template(ES6Connection.ESClientMode.REST).getRestHighLevelClient();
    }


    public String save(String index, String id, String routing, JSONObject obj) throws Exception {
        try {
            IndexRequest request = new IndexRequest("post");
            //存在id字段则使用id
            if (id == null) {
                id = UUID.randomUUID().toString() + "";
            }
            //指定路由增加查询速度
            if (routing == null) {
                routing = "default";
            }
            request.index(index).type(Constant.TYPE).id(id).routing(routing).source(obj.toJSONString(), XContentType.JSON);
            IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            log.info("保存成功！{}", response.toString());
        } catch (Exception e) {
            log.error("{}保存单条数据失败...{},{}", index, e.getMessage(), obj.toJSONString());
            return e.getMessage();
        }
        return "OK";
    }

    public String save(String index, JSONArray array) throws Exception {
        try {

            log.info("array.size()：[{}]", array.size());

            BulkRequest bulkRequest = new BulkRequest();
            IndexRequest request;
            String id;
            for (int i = 0; i < array.size(); i++) {
                request = new IndexRequest("post");
                JSONObject obj = array.getJSONObject(i);
                if (obj.containsKey("id")) {
                    id = obj.getString("id");
                } else {
                    id = UUID.randomUUID().toString() + "";
                }
                request.index(index).type(Constant.TYPE).id(id).source(obj.toJSONString(), XContentType.JSON);
                bulkRequest.add(request);
            }
            BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("批量保存数据成功！{}", response.status().toString());
        } catch (Exception e) {
            log.error("{}保存批量数据失败...{},{}", index, e.getMessage(), array.toJSONString());
            return e.getMessage();
        }
        return "OK";
    }


    public String remove(String index, String id) {
        try {
            DeleteRequest request = new DeleteRequest(index, Constant.TYPE, id);
            DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
            if (response.isFragment()) {
                return "OK";
            }
        } catch (Exception e) {
            log.error("删除数据失败...{},{}", id, e.getMessage());
            return e.getMessage();
        }
        return "ERROR";
    }

    public String select(String index) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(searchResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
