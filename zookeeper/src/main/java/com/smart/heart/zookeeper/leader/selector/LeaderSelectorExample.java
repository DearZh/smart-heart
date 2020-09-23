package com.smart.heart.zookeeper.leader.selector;

import com.google.common.collect.Lists;
import com.smart.heart.zookeeper.ZKClient;
import org.apache.curator.framework.CuratorFramework;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/9/21
 */
public class LeaderSelectorExample {
    private static final int CLIENT_QTY = 2;

    private static final String PATH = "/examples/leader";

    public static void main(String[] args) throws Exception {
        // all of the useful sample code is in ExampleClient.java
        System.out.println("创建" + CLIENT_QTY + "客户，让每个客户协商领导权，然后随机等待几秒钟，然后再进行其他领导者选举。");
        System.out.println("请注意，领导者选举是公平的：所有客户都将成为领导者，并且这样做的次数相同");

        List<CuratorFramework> clients = Lists.newArrayList();
        List<ExampleClient> examples = Lists.newArrayList();

        try {
            for (int i = 0; i < CLIENT_QTY; ++i) {
                CuratorFramework client = ZKClient.instance("10.0.3.18:2181");
                clients.add(client);
                ExampleClient example = new ExampleClient(client, PATH, "Client #" + i);
                examples.add(example);
                example.start();
                System.out.println("初始化完毕");
            }

            System.out.println("Press enter/return to quit\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } finally {
            System.out.println("Shutting down...");
/*
            for (ExampleClient exampleClient : examples) {
                CloseableUtils.closeQuietly(exampleClient);
            }
            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }*/
        }
    }
}
