package com.smart.heart.es6.support;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description: ES 连接器
 * @Author: Arnold.zhao
 * @Date: 2020/8/15
 */
public class ES6Connection {

    /**
     * ES 两种模式
     */
    public enum ESClientMode {
        TRANSPORT, REST
        /**
         * TRANSPORT的tcp链接es的方式，8.0以后似乎就会被官网正式废弃了，但是为了兼容8以下使用TRANSPORT的场景，所以一并封装在Connection类中，
         * 在ES6Connection类中解决对应的ES访问模式的问题，不再暴露到应用层；Arnold.zhao 2020/9/27
         */
    }

    private String ES_HOST = "10.0.4.18:9200";
    private final String CLUSTER_NAME = "elasticsearch";

    protected TransportClient transportClient;

    public RestHighLevelClient restHighLevelClient;

    protected ESClientMode mode;


    public ES6Connection(ESClientMode mode, String... esHost) throws UnknownHostException {
        if (esHost != null && esHost.length > 0) {
            ES_HOST = esHost[0];
        }
        this.mode = mode;
        String[] hosts = ES_HOST.split(",");
        if (mode == ESClientMode.REST) {
            HttpHost[] httpHosts = new HttpHost[hosts.length];
            for (int i = 0; i < hosts.length; i++) {
                String host = hosts[i];
                int j = host.indexOf(":");
                HttpHost httpHost = new HttpHost(InetAddress.getByName(host.substring(0, j)),
                        Integer.parseInt(host.substring(j + 1)));
                httpHosts[i] = httpHost;
            }
            RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);

            restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        } else if (mode == ESClientMode.TRANSPORT) {
            Settings.Builder settingBuilder = Settings.builder();
            settingBuilder.put("cluster.name", CLUSTER_NAME);
            Settings settings = settingBuilder.build();
            transportClient = new PreBuiltTransportClient(settings);
            for (String host : hosts) {
                int i = host.indexOf(":");
                transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName(host.substring(0, i)),
                        Integer.parseInt(host.substring(i + 1))));
            }
        }
    }

    /**
     * 针对不同访问方式获取索引Mapping元数据对象的封装
     *
     * @param indexName
     * @param indexType
     * @return
     * @throws IOException
     */
    public MappingMetaData getMapping(String indexName, String indexType) throws IOException {
        MappingMetaData mappingMetaData = null;
        if (ESClientMode.REST.equals(this.mode)) {
            GetMappingsRequest request = new GetMappingsRequest();
            request.indices(indexName);
            GetMappingsResponse response = restHighLevelClient.indices().getMapping(request, RequestOptions.DEFAULT);
            ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = response.getMappings();
            mappingMetaData = mappings.get(indexName).get(indexType);
        } else if (ESClientMode.TRANSPORT.equals(this.mode)) {
            ImmutableOpenMap<String, MappingMetaData> mappings;
            try {
                mappings = transportClient.admin()
                        .cluster()
                        .prepareState()
                        .execute()
                        .actionGet()
                        .getState()
                        .getMetaData()
                        .getIndices()
                        .get(indexName)
                        .getMappings();
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Not found the mapping info of index: " + indexName);
            }
            mappingMetaData = mappings.get(indexType);
        }
        return mappingMetaData;
    }

    public void close() {
        if (mode == ESClientMode.TRANSPORT) {
            transportClient.close();
        } else if (mode == ESClientMode.REST) {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public RestHighLevelClient getRestHighLevelClient() {
        return restHighLevelClient;
    }

}
