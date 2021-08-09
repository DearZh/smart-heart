package com.smart.heart.es6.example.map;

import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Arnold.zhao <a href="mailto:Arnold_zhao@126.com"/>
 * @create 2021-04-06
 */
public class ManManBulkRequestTest {

    public static void main(String[] args) {
        ES6Template es6Template = null;
        try {
            es6Template = new ES6Template(ES6Connection.ESClientMode.REST, "10.13.67.118:9200");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        BulkRequest bulkRequest = new BulkRequest();

        Map<String, Object> map = new HashMap<>();
        map.put("msg", "bulk_test111");
        //Field [_id] is a metadata field and cannot be added inside a document. Use the index API request parameters.]
//        map.put("_id","11");
        Map<String, String> map1 = new HashMap<>();
        map1.put("msg", "bulk_test122");
//        map1.put("_id","12");
        Map<String, String> map2 = new HashMap<>();
        map2.put("msg", "bulk_test133");
//        map2.put("_id","13");
        IndexRequest indexRequest = new IndexRequest("arnold_test_index_3", "doc", "3dc14310-d2e9-4cf8-9a55-3e0d3826c6bc");
        IndexRequest indexRequest1 = new IndexRequest("arnold_test_index_3", "doc", "021fc0e5-74a2-4239-aa06-8d32022bddc1");
        IndexRequest indexRequest2 = new IndexRequest("arnold_test_index_3", "doc", "304d3ade-f447-4327-a1bb-40c4a3aaca30");

        indexRequest.source(map);
        indexRequest1.source(map1);
        indexRequest2.source(map2);

        bulkRequest.add(indexRequest);
        bulkRequest.add(indexRequest1);
        bulkRequest.add(indexRequest2);

        BulkResponse bulkResponse = null;
        try {
            bulkResponse = es6Template.getRestHighLevelClient().bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bulkResponse.hasFailures()) {
            BulkItemResponse[] bulkItemResponse = bulkResponse.getItems();
            for (int i = 0; i < bulkItemResponse.length; i++) {
                BulkItemResponse itemResponse = bulkItemResponse[i];
                if (!(itemResponse.getFailure() == null)) {
                    System.out.println(itemResponse.getFailureMessage());
                    System.out.println(i);
                    System.out.println(bulkRequest.requests().get(i));
                    IndexRequest indexRequest3 = (IndexRequest) bulkRequest.requests().get(i);
                    es6Template.getRestHighLevelClient().indexAsync(indexRequest3, RequestOptions.DEFAULT,
                            new ActionListener<IndexResponse>() {
                                @Override
                                public void onResponse(IndexResponse indexResponse) {
                                    System.out.println("Success：" + indexResponse.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.out.println("ERROR：" + e.getMessage());
                                }
                            });
                }

            }
        }


    }
}
