package com.smart.heart.es6.support;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
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
    }

    private final String ES_HOST = "10.0.2.10:9200,10.0.2.11:9200,10.0.2.12:9200";
    private final String CLUSTER_NAME = "elasticsearch";

    private TransportClient transportClient;

    private RestHighLevelClient restHighLevelClient;

    private ESClientMode mode;

    public ES6Connection(ESClientMode mode) throws UnknownHostException {
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

}
