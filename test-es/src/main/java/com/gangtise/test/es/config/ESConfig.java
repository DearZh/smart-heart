package com.gangtise.test.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/3/10
 */
@Configuration
public class ESConfig {

    @Value("#{'${es.host}'.split(',')}")
    public String[]  host;

    @Value("${es.port}")
    public int  port;

    @Value("${es.scheme}")
    public String  scheme;

    @Bean
    public RestClientBuilder restClientBuilder() {
        return RestClient.builder(makeHttpHost());
    }

    @Bean
    public RestClient restClient(){
        return RestClient.builder(makeHttpHost()).build();
    }

    /**
     * 组装集群
     * @return
     */
    private HttpHost[] makeHttpHost() {
        HttpHost[] hosts = new HttpHost[host.length];
        for(int i=0;i<hosts.length ; i++) {
            hosts[i] = new HttpHost(host[i],port,scheme);
        }
        return hosts;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(@Autowired RestClientBuilder restClientBuilder){
        return new RestHighLevelClient(restClientBuilder);
    }


}
