package com.smart.heart.es6;

import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/10/14
 */
public class GetTest {
    public static void main(String[] args) throws IOException {
        /*ES6Template es6Template = new ES6Template(ES6Connection.ESClientMode.REST);
        GetRequest getRequest = new GetRequest("twitter");
        getRequest.id("1");
        GetResponse getResponse = es6Template.restHighLevelClient.get(getRequest);
        System.out.println(getResponse);
        System.out.println(getResponse.getSourceAsMap());
        System.out.println(getResponse.isExists());

        */
        System.out.println("test-consumer-group-arnold-1".hashCode()%50);
        System.out.println("test-consumer-group-arnold-1".hashCode());
    }
}
