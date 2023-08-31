package com.smart.heart.kafka.verification;

import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;

import java.util.Properties;

/**
 * @Description: 创建Topic, 指定对应主题的分区和副本数创建
 * @Author: Arnold.zhao
 * @Date: 2020/12/4
 */
public class CreateTopic {

    /**
     * 同kafka-topics.sh --create 命令一致，只需要指定zk地址即可，无需指定kafka host地址
     * ./kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic arnold_test_java_1
     *
     * @param args
     */

    public static void main(String[] args) {
        String zk_host = "10.0.3.17:2181";
        int session_timeout = 30000;
        int connection_timeout = 30000;
        String topic = "arnold_test_java_1";
        //指定分区数
        int partition = 3;
        //指定每个分区的副本数
        int replication_factor = 3;
        //主题创建时无需指定properties信息，尤其不要把生产者的Properties配置拿过来给主题创建时使用；
        Properties properties = new Properties();

        //*********************************************************************************************************

        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(zk_host, session_timeout, connection_timeout, JaasUtils.isZkSecurityEnabled());
            //验证当前topic是否存在
            boolean flag = AdminUtils.topicExists(zkUtils, topic);
            if (!flag) {
                //不存在该主题重新进行创建；
                AdminUtils.createTopic(zkUtils, topic, partition, replication_factor, properties, AdminUtils.createTopic$default$6());
            } else {
                System.out.println("主题已存在无需重复创建");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zkUtils.close();
        }

    }
}
