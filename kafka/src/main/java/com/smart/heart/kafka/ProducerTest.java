package com.smart.heart.kafka;

import com.smart.heart.kafka.config.KafkaProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/10/10
 */
public class ProducerTest {

    static String servers = "10.0.3.20:9092,10.0.3.24:9092,10.0.3.26:9092";
    static String topic = "canal_test";

    public static void main(String[] args) throws InterruptedException {

        Properties properties = KafkaProducerConfig.properties(servers);
        Producer<String, String> producer = new KafkaProducer<String, String>(properties);

        int i = 0;
        while (true) {
            i++;
            String message = "canal_json_test" + i;
            producer.send(new ProducerRecord<>(topic, message));
            Thread.sleep(1000);
        }
    }
}
/**
 * 消费者：
 * https://blog.csdn.net/a2011480169/article/details/82795157
 * https://blog.csdn.net/onway_goahead/article/details/103584024
 * https://blog.csdn.net/lishuangzhe7047/article/details/74530417/
 * 生产者问题：
 * 生产者自定义分区，生产数据
 * 生产者自定义分区策略
 *
 */
