package com.smart.heart.es6.example.map;

import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author Arnold.zhao
 * @create 2021-04-15
 */
public class ManManGetTest {

    public static void main(String[] args) {

        ES6Template es6Template = null;
        try {
            es6Template = new ES6Template(ES6Connection.ESClientMode.REST, "10.13.67.118:9200");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        GetRequest getRequest = new GetRequest("app-log-20100106");
        getRequest.type("doc");
        getRequest.id("AXhdm-WgPoLTOfIxDy-p");

        try {
            GetResponse getResponse = es6Template.getRestHighLevelClient().get(getRequest, RequestOptions.DEFAULT);
            System.out.println(getResponse);
            System.out.println(getResponse.isExists());
            System.out.println(getResponse.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
