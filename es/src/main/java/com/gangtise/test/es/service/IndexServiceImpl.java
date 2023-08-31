package com.gangtise.test.es.service;

import com.alibaba.fastjson.JSONObject;
import com.gangtise.test.es.constant.Constant;
import com.gangtise.test.es.controller.Tools;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/3/11
 */
@Slf4j
@Service
public class IndexServiceImpl {


    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public boolean existsIndex(String index) {
        try {
            GetIndexRequest getRequest = new GetIndexRequest().indices(index);
            getRequest.local(false);
            getRequest.humanReadable(true);
            return restHighLevelClient.indices().exists(getRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("IndexServiceImpl.existsIndex error...{}", e.getMessage());
        }
        return false;
    }


    public Set<String> getAllIndexName() {
        Set<String> indices = null;
        try {
            GetAliasesRequest request = new GetAliasesRequest();
            GetAliasesResponse getAliasesResponse = restHighLevelClient.indices().getAlias(request, RequestOptions.DEFAULT);
            Map<String, Set<AliasMetaData>> map = getAliasesResponse.getAliases();
            indices = map.keySet();
        } catch (IOException e) {
            log.error("IndexServiceImpl.getAllIndexName error...{}", e.getMessage());
        }
        return indices;
    }


    public String createEsIndex(String indexName) {
        if (indexName == null || indexName.equals("")) {
            return "index is null";
        }
        //创建索引
        if (existsIndex(indexName)) {
            log.warn("{}的索引已经存在！", indexName);
            return "index：" + indexName + " Already exists";
        } else {
            try {
                //配置文件
                ClassPathResource seResource = new ClassPathResource("json/setting.json");
                InputStream seInputStream = seResource.getInputStream();
                String seJson = Tools.getFileContent(seInputStream);
                seInputStream.close();
                ClassPathResource mpResource = new ClassPathResource("json/" + indexName + "-mapping.json");
                InputStream mpInputStream = mpResource.getInputStream();
                String mpJson = Tools.getFileContent(mpInputStream);
                seInputStream.close();

                createIndex(indexName.toLowerCase(), seJson, mpJson);
            } catch (Exception e) {
                log.error("创建索引出现异常！{}", e.getMessage());
                return e.getMessage();
            }
        }

        //获取索引Mapping配置信息
        String str = getIndexMappingInfo(indexName);
        log.info("Mapping配置信息" + str);
        //获取索引Setting配置信息
        String strTest = getIndexSettingInfo(indexName);
        log.info("Setting配置信息" + strTest);

        return "OK";
    }

    public void createIndex(String index, String setting, String mapping) {
        try {
            CreateIndexRequest cir = new CreateIndexRequest(index);
            cir.settings(setting, XContentType.JSON);
            //type设置为_doc，7.X只能是_doc
            //7.x可以设置为 _doc 或 _create
            cir.mapping(Constant.TYPE, mapping, XContentType.JSON);
            //设置别名
//            cir.alias(new Alias(index+"_alias"));

            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(cir, RequestOptions.DEFAULT);
            boolean falg = createIndexResponse.isAcknowledged();
            if (falg) {
                System.out.println("创建索引库:" + index + "成功！");
            }
        } catch (IOException e) {
            log.error("IndexServiceImpl.createIndex error...{}", e.getMessage());
        }
    }


    public String removeIndex(String index) {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(index);
            AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
            return response.isAcknowledged() ? "OK" : "ERROR";
        } catch (IOException e) {
            log.error("IndexServiceImpl.removeIndex error...{}", e.getMessage());
            return e.getMessage();
        }
    }


    public String getIndexMappingInfo(String index) {
        try {
            GetIndexRequest getRequest = new GetIndexRequest().indices(index);
            GetIndexResponse response = restHighLevelClient.indices().get(getRequest, RequestOptions.DEFAULT);
            ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappingMap = response.getMappings();
            JSONObject jsonObject = Tools.getMappingObject(index, mappingMap);
            return jsonObject.toJSONString();
        } catch (Exception e) {
            log.error("IndexServiceImpl.getIndexMappingInfo error...{}", e.getMessage());
            return "";
        }
    }


    public String getIndexSettingInfo(String index) {
        try {
            GetIndexRequest getRequest = new GetIndexRequest().indices(index);
            GetIndexResponse response = restHighLevelClient.indices().get(getRequest, RequestOptions.DEFAULT);
            ImmutableOpenMap<String, Settings> settingMap = response.getSettings();
            Settings settings = settingMap.get(index);
            JSONObject jsonObject = JSONObject.parseObject(settings.toString());
            return jsonObject.toJSONString();
        } catch (Exception e) {
            log.error("IndexServiceImpl.getIndexSettingInfo error...{}", e.getMessage());
            return "";
        }
    }


/*    public static void main(String[] args) {
        static Object obj = null;


        new Thread(new Runnable() {
            @Override
            public void run() {
                Object obj1 = null;
                obj = 2323;
            }
        }).start();
    }*/

}
