package com.smart.heart.kafka.verification.consumer;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 查询指定kafka partition & offset 下对应的message
 *
 * @author Arnold.zhao
 * @version PartitionOffsetConsumer.java, v 0.1 2021-04-29 14:59 Arnold.zhao Exp $$
 */
public class PartitionOffsetConsumer {

    public static void main(String[] args) {
        String topicName = "gatewayAllRequestLog";
        int partition = 1;
        int offset = 36944402;

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.13.66.120:9092");
        //lion kafka.gateway.zookeeper
        properties.setProperty("zookeeper.connect", "10.13.66.120:2181,10.13.66.121:2181,10.13.66.122:2181");


        //lion legoo.gateway.consumerGroup
        String consumerGroup = "gatewayTmp2";

        //如果partition和offset不存在，抛出异常
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "none");
        //消费者组名称
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "legoo_" + consumerGroup);
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        Object result = null;
        Object detailData = null;
        try {
            TopicPartition topicPartition = new TopicPartition(topicName, partition);
            //指定所要消费的主题
            consumer.assign(Arrays.asList(topicPartition));
            //指定消费的分区及偏移量
            consumer.seek(topicPartition, offset);

            @SuppressWarnings("rawtypes")
            ConsumerRecords records = consumer.poll(10000);

            if (records != null) {
                List consumerRecordsList = records.records(topicPartition);
                if (consumerRecordsList != null) {
                    if (consumerRecordsList.get(0) instanceof ConsumerRecord) {
                        result = ((ConsumerRecord) consumerRecordsList.get(0)).value();
                        if (null != result && result instanceof String) {
                            detailData = JSONObject.parseObject((String) result);
                        }
                    } else {
                        detailData = consumerRecordsList.get(0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            detailData = result;

        } finally {
            consumer.commitSync();
            consumer.close();
        }

        System.out.println(detailData.toString());
    }
}
