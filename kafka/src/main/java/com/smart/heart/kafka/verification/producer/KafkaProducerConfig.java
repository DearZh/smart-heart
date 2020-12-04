package com.smart.heart.kafka.verification.producer;

import java.util.Properties;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/10/14
 */
public class KafkaProducerConfig {

    //https://kafka.apachecn.org/documentation.html#topicconfigs 中文kafka官网，但其实里面大部分核心内容还是英文,关于配置的说明很详细

    /**
     * 生产者要发送消息，并不是直接发送给服务端，而是现在客户端把消息放入队列中，然后由一个消息发送线程从队列中拉取消息，以批量的方式发送给服务端；
     * Kafka的记录收集器（RecordAccumulator）负责缓存生产者客户端发送的消息，发送线程（Send）负责读取记录收集器的批量信息；
     *
     * @param servers
     * @return
     */

    public static Properties properties(String servers) {

        Properties props = new Properties();
        // 指定bootstrap.servers属性。必填，无默认值。用于创建向kafka broker服务器的连接。
        props.put("bootstrap.servers", servers);
        // 指定key.serializer属性。必填，无默认值。被发送到broker端的任何消息的格式都必须是字节数组。
        // 因此消息的各个组件都必须首先做序列化，然后才能发送到broker。该参数就是为消息的key做序列化只用的。
        props.put("key.serializer", org.apache.kafka.common.serialization.StringSerializer.class);
        // 指定value.serializer属性。必填，无默认值。和key.serializer类似。此被用来对消息体即消息value部分做序列化。
        // 将消息value部分转换成字节数组。
        props.put("value.serializer", org.apache.kafka.common.serialization.StringSerializer.class);
        //acks参数用于控制producer生产消息的持久性（durability）。参数可选值，0、1、-1（all）。
        //重要：ack的配置决定了生产者消息的可靠性，详情参考之前写的一篇文章： https://www.cnblogs.com/zh94/p/14086349.html
        props.put("acks", "-1");
        //props.put(ProducerConfig.ACKS_CONFIG, "1");
        //在producer内部自动实现了消息重新发送。默认值0代表不进行重试。
        props.put("retries", 3);
        //props.put(ProducerConfig.RETRIES_CONFIG, 3);
        //调优producer吞吐量和延时性能指标都有非常重要作用。默认值16384即16KB。
        props.put("batch.size", 323840);

        //props.put(ProducerConfig.BATCH_SIZE_CONFIG, 323840);
        //控制消息发送延时行为的，该参数默认值是0。表示消息需要被立即发送，无须关系batch是否被填满。
        props.put("linger.ms", 10);
        //props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        //指定了producer端用于缓存消息的缓冲区的大小，单位是字节，默认值是33554432即32M。
        props.put("buffer.memory", 33554432);
        //props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put("max.block.ms", 3000);
        return props;
    }
}
