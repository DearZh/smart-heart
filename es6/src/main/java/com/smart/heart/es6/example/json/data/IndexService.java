package com.smart.heart.es6.example.json.data;


import com.alibaba.fastjson.JSONObject;
import com.smart.heart.es6.example.json.constant.Constant;
import com.smart.heart.es6.support.ES6Connection;
import com.smart.heart.es6.support.template.ES6Template;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;


/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/3/11
 */
public class IndexService {

    private final static Logger log = LoggerFactory.getLogger(IndexService.class);

    private static RestHighLevelClient restHighLevelClient;

    public IndexService() throws UnknownHostException {
        restHighLevelClient = new ES6Template(ES6Connection.ESClientMode.REST).getRestHighLevelClient();
    }

    public static void main(String[] args) throws UnknownHostException {
        new IndexService().createEsIndex("httpnews");
    }


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
                /*URL url = ClassLoader.getSystemResource("json/setting.json");
                File file = new File(url.toURI());
                InputStream inputStream = new FileInputStream(file);
                */
                ClassPathResource seResource = new ClassPathResource("json/setting.json");

                InputStream seInputStream = seResource.getInputStream();
                String seJson = getFileContent(seInputStream);
                seInputStream.close();
                ClassPathResource mpResource = new ClassPathResource("json/" + indexName + "-mapping.json");
                InputStream mpInputStream = mpResource.getInputStream();
                String mpJson = getFileContent(mpInputStream);
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
            JSONObject jsonObject = getMappingObject(index, mappingMap);
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

    public static String getFileContent(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static JSONObject getMappingObject(String index, ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappingMap) {
        try {
            ImmutableOpenMap<String, MappingMetaData> typeMap = mappingMap.get(index);
            MappingMetaData propertieMap = typeMap.get(Constant.TYPE);
            Map<String, Object> m = propertieMap.getSourceAsMap();
            String json = JSONObject.toJSONString(m);
            JSONObject jsonObject = JSONObject.parseObject(json);
            return jsonObject;
        } catch (Exception e) {
            log.error("获取Mapping对象时出现异常...{}", e.getMessage());
            return null;
        }
    }
}
