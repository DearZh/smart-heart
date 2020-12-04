package com.smart.heart.kafka.verification;

import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;

/**
 * @Description: 主题删除
 * @Author: Arnold.zhao
 * @Date: 2020/12/4
 */
public class DeleteTopic {
    public static void main(String[] args) {

        String zk_host = "10.0.3.17:2181";
        int session_timeout = 30000;
        int connection_timeout = 30000;

        String topic = "arnold_test_java_1";


        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(zk_host, session_timeout, connection_timeout, JaasUtils.isZkSecurityEnabled());
            AdminUtils.deleteTopic(zkUtils,topic);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zkUtils.close();
        }
    }
}
