package com.smart.heart.es6.example.map;

import com.alibaba.fastjson.JSONObject;
import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/10/14
 */
public class GetTest {
    public static void main(String[] args) throws IOException {
        /*ES6Template es6Template = new ES6Template(ES6Connection.ESClientMode.REST, "10.69.0.4:9200");
        GetRequest getRequest = new GetRequest("cls_lian_v1_article");
        getRequest.id("1");
        GetResponse getResponse = es6Template.getRestHighLevelClient().get(getRequest);
        System.out.println(getResponse);
        System.out.println(getResponse.getSourceAsMap());
        System.out.println(getResponse.isExists());

        System.out.println("test-consumer-group-arnold-1".hashCode() % 50);
        System.out.println("test-consumer-group-arnold-1".hashCode());
        */

        JSONObject log = new JSONObject();
        log.put("A", "A");
        IndexRequest indexRequest = new IndexRequest().index("key").type("doc").id(UUID.randomUUID().toString())
                .source(log.toJSONString(), XContentType.JSON);
        Map<String, Object> map1 = indexRequest.sourceAsMap();
        map1.put("_id", indexRequest.id());
        map1.forEach((k, v) -> {
            System.out.println("key：" + k + ">v：" + v);
        });

    }
}
