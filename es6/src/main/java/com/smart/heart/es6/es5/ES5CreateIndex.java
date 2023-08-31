package com.smart.heart.es6.es5;

import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/12/2
 */
public class ES5CreateIndex {
    static RestHighLevelClient restHighLevelClient = null;

    public static void main(String[] args) throws Exception {

        ES6Template es6Template = new ES6Template(ES6Connection.ESClientMode.REST, "10.0.2.13:9200");
        restHighLevelClient = es6Template.getRestHighLevelClient();


//        createIndex("cls_article_test", mapping);
//        System.out.println(existsIndex("cls_article_test"));
//        System.out.println(removeIndex("cls_lian_v1_article_copy6"));

//        indexRequest("cls_article_test");
//        System.out.println(isIndexData("cls_article_test", "6"));
//        bulkIndexRequest("cls_article_test");
        bulkUpdateRequest("cls_article_test");
        //基本操作验证完好像没什么问题，需要注意的就是，6.0以后默认的 _doc 索引类型，在5.0不能使用，需要改变索引类型，改变为 doc或者随便定义类型都行，但不能是 _doc ，其它好像API层面变化不是很大，

    }

    public static void bulkUpdateRequest(String indexName) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();
        UpdateRequest updateRequest = new UpdateRequest(indexName, "doc", "4");
        Map<String, Object> map = new HashMap<>();
        map.put("img", "Arnold12");
        map.put("type", "5");
        map.put("title", "11shanghai1");
        updateRequest.doc(map);
        bulkRequest.add(updateRequest);
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    public static void bulkIndexRequest(String indexName) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();
        IndexRequest indexRequest = new IndexRequest(indexName, "doc", "4");
        Map<String, Object> map = new HashMap<>();
        map.put("img", "Arnold1");
        map.put("type", "5");
        map.put("title", "11shanghai1");
        indexRequest.source(map);
        bulkRequest.add(indexRequest);
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    //新增数据
    public static boolean indexRequest(String indexName) throws Exception {
//        UpdateRequest indexRequest = new UpdateRequest("twitter", "doc", "4");
        IndexRequest indexRequest = new IndexRequest(indexName, "doc", "5");
        Map<String, Object> map = new HashMap<>();
        map.put("img", "Arnold");
//        map.put("_id", "5");
        map.put("type", "5");
        map.put("title", "11shanghai");
//        indexRequest.doc(map);
        indexRequest.source(map);
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        return false;
    }

    public static boolean isIndexData(String indexName, String id) throws Exception {
        GetRequest getRequest = new GetRequest(indexName);
        getRequest.id(id);
        GetResponse getResponse = restHighLevelClient.get(getRequest);
        return getResponse.isExists();
    }

    public static boolean removeIndex(String indexName) throws Exception {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        try {
            DeleteIndexResponse deleteIndexResponse = restHighLevelClient.indices().delete(deleteIndexRequest);
            if (deleteIndexResponse.isAcknowledged()) {
                return true;
            }
        } catch (IOException e) {
            throw e;
        }
        return false;
    }

    public static boolean createIndex(String indexName, String mapping) throws Exception {
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.source(mapping, XContentType.JSON);
            System.out.println(createIndexRequest.toString());
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            if (createIndexResponse.isAcknowledged()) {
                return true;
            }
        } catch (IOException e) {
            throw e;
        }
        return false;
    }

    //判断index是否存在
    public static boolean existsIndex(String indexName) throws Exception {
        try {
            GetIndexRequest getRequest = new GetIndexRequest().indices(indexName);
            return restHighLevelClient.indices().exists(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw e;
        }
    }

    static String mapping = "{\n" +
            "    \"mappings\": {\n" +
            "        \"doc\": {\n" +
            "            \"properties\": {\n" +
            "                \"img\": {\n" +
            "                    \"type\": \"keyword\"\n" +
            "                },\n" +
            "                \"recommend\": {\n" +
            "                    \"type\": \"integer\"\n" +
            "                },\n" +
            "                \"remark\": {\n" +
            "                    \"type\": \"text\"\n" +
            "                },\n" +
            "                \"markettrend\": {\n" +
            "                    \"type\": \"integer\"\n" +
            "                },\n" +
            "                \"title\": {\n" +
            "                    \"type\": \"text\"\n" +
            "                },\n" +
            "                \"type\": {\n" +
            "                    \"type\": \"integer\"\n" +
            "                },\n" +
            "                \"content\": {\n" +
            "                    \"type\": \"text\"\n" +
            "                },\n" +
            "                \"jpush\": {\n" +
            "                    \"type\": \"integer\"\n" +
            "                },\n" +
            "                \"stocks\": {\n" +
            "                    \"type\": \"text\"\n" +
            "                },\n" +
            "                \"modifiedtime\": {\n" +
            "                    \"type\": \"long\"\n" +
            "                },\n" +
            "                \"ctime\": {\n" +
            "                    \"type\": \"long\"\n" +
            "                },\n" +
            "                \"id\": {\n" +
            "                    \"type\": \"integer\"\n" +
            "                },\n" +
            "                \"brief\": {\n" +
            "                    \"type\": \"text\"\n" +
            "                },\n" +
            "                \"createtime\": {\n" +
            "                    \"type\": \"long\"\n" +
            "                },\n" +
            "                \"author\": {\n" +
            "                    \"type\": \"text\"\n" +
            "                },\n" +
            "                \"topics\": {\n" +
            "                    \"type\": \"keyword\"\n" +
            "                },\n" +
            "                \"isfree\": {\n" +
            "                    \"type\": \"integer\"\n" +
            "                },\n" +
            "                \"freetop\": {\n" +
            "                    \"type\": \"integer\"\n" +
            "                },\n" +
            "                \"bold\": {\n" +
            "                    \"type\": \"integer\"\n" +
            "                },\n" +
            "                \"classification\": {\n" +
            "                    \"type\": \"keyword\"\n" +
            "                },\n" +
            "                \"audiourl\": {\n" +
            "                    \"type\": \"keyword\"\n" +
            "                },\n" +
            "                \"jieshuo\": {\n" +
            "                    \"type\": \"text\"\n" +
            "                },\n" +
            "                \"linkurl\": {\n" +
            "                    \"type\": \"keyword\"\n" +
            "                },\n" +
            "                \"updatetime\": {\n" +
            "                    \"type\": \"long\"\n" +
            "                },\n" +
            "                \"status\": {\n" +
            "                    \"type\": \"integer\"\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";
}
