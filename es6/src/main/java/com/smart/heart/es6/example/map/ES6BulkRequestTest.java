package com.smart.heart.es6.example.map;

import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 测试ES BulkRequest批量提交
 * @Author: Arnold.zhao
 * @Date: 2020/8/15
 */
public class ES6BulkRequestTest {

    public static void main(String[] args) throws Exception {

        ES6Template es6Template = new ES6Template(ES6Connection.ESClientMode.REST);

        BulkRequest bulkRequest = new BulkRequest();

//        UpdateRequest indexRequest = new UpdateRequest("twitter", "doc", "4");
        IndexRequest indexRequest = new IndexRequest("twitter", "doc", "5");
        Map<String, Object> map = new HashMap<>();
        map.put("user", "Arnold");
        map.put("_id", "5");
        map.put("uid", "5");
        map.put("city", "11shanghai");
//        indexRequest.doc(map);
        indexRequest.source(map);
        /*
        IndexResponse indexResponse = es6Template.restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        */
        bulkRequest.add(indexRequest);

        System.out.println(bulkRequest.numberOfActions());

        BulkResponse bulkResponse = es6Template.getRestHighLevelClient().bulk(bulkRequest);
        System.out.println(bulkResponse.hasFailures());
        for (BulkItemResponse bulkItemResponse : bulkResponse.getItems()) {
            throw new RuntimeException(bulkItemResponse.getFailureMessage());
//            System.out.println(bulkResponse.getItems()[0].getFailure().getStatus());
        }


    }
}
