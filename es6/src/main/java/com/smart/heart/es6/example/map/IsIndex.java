package com.smart.heart.es6.example.map;

import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author Arnold.zhao
 * @version isIndex.java, v 0.1 2021-05-11 10:00 Arnold.zhao Exp $$
 */
public class IsIndex {

    public static void main(String[] args) {

        ES6Template es6Template = null;
        try {
            es6Template = new ES6Template(ES6Connection.ESClientMode.REST, "10.13.67.118:9200");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        GetIndexRequest getIndexRequest = new GetIndexRequest();
        getIndexRequest.indices(new String[]{"dev-log-trade-feed-app-2021.05.10_1"});
        try {
            /*
            GetIndexResponse getIndexResponse = es6Template.getRestHighLevelClient().indices().get(getIndexRequest, RequestOptions.DEFAULT);
            System.out.println(getIndexResponse);*/
            boolean flag = es6Template.getRestHighLevelClient().indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            System.out.println(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
