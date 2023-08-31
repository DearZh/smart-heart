package com.smart.heart.zookeeper.leader.latch;

import com.smart.heart.zookeeper.ZKClient;
import org.apache.curator.framework.CuratorFramework;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/9/21
 */
public class LatchDemo {
    private static final String PATH = "/examples/leader";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = ZKClient.instance("10.0.3.18:2181");
        /*for (int i = 0; i < 5; i++) {
            ExampleClient exampleClient = new ExampleClient("Test# " + i, PATH, client);
            exampleClient.start();
            System.out.println("初始化完毕");
        }*/
        ExampleClient exampleClient = new ExampleClient("Test# " + 1, PATH, client);
        System.out.println("节点状态：>>>>> " + exampleClient.hasLeadership());
        exampleClient.start();
        System.out.println("初始化完毕1");
        exampleClient.start();
        System.out.println("初始化完毕2");

        for (int i = 0; i < 5; i++) {
            System.out.println(exampleClient.getName() + " >>> " + exampleClient.hasLeadership());
            Thread.sleep(3000);
        }
        System.out.println("Press enter/return to quit\n");
        new BufferedReader(new InputStreamReader(System.in)).readLine();
    }
}
