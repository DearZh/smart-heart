package com.smart.heart.es6.example.map;

import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;

import java.io.IOException;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/10/14
 */
public class GetTest {
    public static void main(String[] args) throws IOException {
        ES6Template es6Template = new ES6Template(ES6Connection.ESClientMode.REST, "10.69.0.4:9200");
        GetRequest getRequest = new GetRequest("cls_lian_v1_article");
        getRequest.id("1");
        GetResponse getResponse = es6Template.getRestHighLevelClient().get(getRequest);
        System.out.println(getResponse);
        System.out.println(getResponse.getSourceAsMap());
        System.out.println(getResponse.isExists());

        System.out.println("test-consumer-group-arnold-1".hashCode() % 50);
        System.out.println("test-consumer-group-arnold-1".hashCode());
    }
}
