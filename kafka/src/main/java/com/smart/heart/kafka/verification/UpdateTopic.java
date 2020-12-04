package com.smart.heart.kafka.verification;

import kafka.admin.AdminUtils;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;

import java.util.Properties;

/**
 * @Description: 修改主题配置
 * @Author: Arnold.zhao
 * @Date: 2020/12/4
 */
public class UpdateTopic {

    public static void main(String[] args) {
        String zk_host = "10.0.3.17:2181";
        int session_timeout = 30000;
        int connection_timeout = 30000;
        String topic = "arnold_test_java_1";
        //定义新的Topic properties配置信息
        Properties updateProperties = new Properties();


        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(zk_host, session_timeout, connection_timeout, JaasUtils.isZkSecurityEnabled());
            //获取已有的Topic配置,此处查询主题级别的配置，因为指定类型为 ConfigType.Topic()
            Properties properties = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topic);
            System.out.println(properties.toString());
            //融合为最新的Properties配置
            properties.putAll(updateProperties);
            //替换当前的Topic的主题配置信息
            AdminUtils.changeTopicConfig(zkUtils, topic, properties);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zkUtils.close();
        }

    }
}
