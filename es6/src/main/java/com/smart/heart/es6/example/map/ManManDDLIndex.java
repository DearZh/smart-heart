package com.smart.heart.es6.example.map;

import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author Arnold.zhao
 * @version ManManDDLIndex.java, v 0.1 2021-05-14 10:31 Arnold.zhao Exp $$
 */
public class ManManDDLIndex {

    public static void main(String[] args) {
        ES6Template es6Template = null;
        try {
            es6Template = new ES6Template(ES6Connection.ESClientMode.TRANSPORT, "10.13.67.118:9200");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        RestHighLevelClient restHighLevelClient = es6Template.getRestHighLevelClient();
        UpdateSettingsRequest request = new UpdateSettingsRequest("dev-log-arnoldtest_3");
        Settings.builder().put("index.number_of_replicas", "1").build();

        request.settings(Settings.builder().put("index.number_of_replicas", "0"));
        /*try {
            UpdateSettingsResponse updateSettingsResponse = restHighLevelClient.indices().putSettings(request, RequestOptions.DEFAULT);
            System.out.println(updateSettingsResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        ActionFuture<UpdateSettingsResponse> actionFuture = es6Template.getTransportClient().admin().indices().updateSettings(request);
        System.out.println(actionFuture.actionGet().toString());

    }
}
