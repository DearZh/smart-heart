package com.smart.heart.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/8/12
 */
public class ZKClient {
    public static CuratorFramework instance(String... host) {
        String hosts = "10.0.4.14:2181";
        if (host.length != 0) {
            //10.0.4.11:2181,10.0.4.13:2181,
            hosts = host[0];
        }
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(hosts)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .sessionTimeoutMs(6000)
                .connectionTimeoutMs(3000)
                .namespace("smart-heart")
                .build();
        curatorFramework.start();
        return curatorFramework;
    }
}
