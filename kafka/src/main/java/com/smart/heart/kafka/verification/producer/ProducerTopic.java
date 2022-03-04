package com.smart.heart.kafka.verification.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Description: 生产者验证
 * @Author: Arnold.zhao
 * @Date: 2020/10/10
 */
public class ProducerTopic {

    static String servers = "10.13.67.210:9092";
    static String topic = "ymm-applog-dev-test";

    //https://cloud.tencent.com/developer/article/1558924
    //https://www.cnblogs.com/lshan/p/11544111.html

    /**
     * 默认Kafka生产者的轮训策略以及按照Key值进行分区的策略是已经足够的，如果需要在生产数据时，指定分区进行数据插入，则可以在实例化ProducerRecord()对象时，进行分区的指定：
     * ProducerRecord producerRecord = new ProducerRecord<>(topic, 4, message)   //指定分区数和 value
     * ProducerRecord producerRecord = new ProducerRecord<>(topic, "key", message);//指定消息的key 和 value
     * ProducerRecord producerRecord = new ProducerRecord<>(topic, 4, "key", message); //指定消息的分区数，key和value
     * <p>
     * 如果不直接指定分区进行数据插入，则可以实现Partitioner.class 类，重写分区的路由策略;
     * 参考这个：https://www.cnblogs.com/listenfwind/p/12465409.html
     */

    /**
     * kafka client源码解析： https://www.infoq.cn/article/yijv9mh3nqusacwsxjlw
     */
    public static void main(String[] args) throws InterruptedException {
        Properties properties = KafkaProducerConfig.properties(servers);
        Producer<String, String> producer = new KafkaProducer<String, String>(properties);

        int i = 0;
        while (true) {
            i++;
            String message = "canal_json_test" + i;
            ProducerRecord producerRecord = new ProducerRecord<>(topic, message);

            Future<RecordMetadata> future = producer.send(producerRecord);
            //异步发送，直接回调Callback
            /*Future<RecordMetadata> future1 = producer.send(new ProducerRecord<>(topic, message), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {

                }
            });
            */
            try {
                RecordMetadata recordMetadata = future.get();
                System.out.println(recordMetadata);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Thread.sleep(1000);
        }
    }
}
