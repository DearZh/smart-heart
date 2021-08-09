package com.smart.heart.kafka.verification.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/10/14
 */
public class ConsumerTopic {


    public static void main(String[] args) {
        String topicName = "canal_block_component_latest_pamir";
        Properties props = new Properties();
//        props.put("bootstrap.servers", "10.0.3.20:9092,10.0.3.24:9092,10.0.3.26:9092");
        props.put("bootstrap.servers", "10.0.4.11:9092,10.0.4.13:9092,10.0.4.14:9092");
        //offset是否是自动提交，还是手动提交（false）
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        /**
         * earliest
         * 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费；
         * latest
         * 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
         * none
         * topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
         *
         * 通过上述三种配置方式可知，默认情况下，只要该分区下该消费者组有提交offset，
         * 则该消费者组重新启动后则也是从提交的offset位置处开始消费数据；
         * 如果该消费者组对应该分区没有提交的offset，则按照所配置的规范来消费数据；
         * 例：group1 消费组，0-10的数据消费到了5条，此时group1消费组stop；然后重新start，则还是从 5条出开始消费。
         * 此时，新建了一个group2消费组，由于配置为latest且当前该group2消费组对应分区无提交的offset，则从该分区下的最新数据进行消费。
         */
        props.put("auto.offset.reset", "latest");
        props.put("request.timeout.ms", "40000");
        props.put("session.timeout.ms", "30000");
        props.put("isolation.level", "read_committed");
        props.put("max.poll.records", 1000);
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", StringDeserializer.class);
        //
        props.put("group.id", "canal_test_group");
        props.put("client.id", UUID.randomUUID().toString().substring(0, 6));

        /**
         *
         *
         */
        Map<Integer, Long> currentOffsets = new ConcurrentHashMap<>();


        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(props);
        //指定所要消费的主题
//        kafkaConsumer.subscribe(Collections.singletonList("canal_test"));

        Map<TopicPartition, OffsetAndMetadata> offsetAndMetadataMap = new HashMap<>();
        //指定Topic和分区
        TopicPartition topicPartition = new TopicPartition(topicName, 0);
        //指定偏移量消费
        OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(198041);
        offsetAndMetadataMap.put(topicPartition, offsetAndMetadata);
        //修改偏移量
        kafkaConsumer.commitSync(offsetAndMetadataMap);
        //指定所要消费的主题
        kafkaConsumer.assign(Arrays.asList(topicPartition));

        while (true) {

            ConsumerRecords<String, String> records = (ConsumerRecords<String, String>) kafkaConsumer.poll(TimeUnit.MILLISECONDS.toMillis(1000));
            if (!records.isEmpty()) {
                for (ConsumerRecord<String, String> record : records) {
                    String message = record.value();
                    //记录所对应的分区 和偏移量
                    currentOffsets.put(record.partition(), record.offset());
                    System.out.println(record.partition() + "___" + record.offset() + "___" + message);
                }
            }/* else {
                //如果获取不到数据，则重新指定分区和数据偏移量进行数据获取
                System.out.println("---------------------------");
                //重新指定Topic&分区 以及偏移量更改为10，
                kafkaConsumer.seek(new TopicPartition(topicName, 0), 10);
            }*/
            //提交offset的修改
            kafkaConsumer.commitSync();
        }
    }
}
