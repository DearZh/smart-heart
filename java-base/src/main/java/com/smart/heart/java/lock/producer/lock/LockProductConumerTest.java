package com.smart.heart.java.lock.producer.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/28
 */
public class LockProductConumerTest {
    static ReentrantLock reentrantLock = new ReentrantLock();
    //这个conditionPro 用来表示生产者的等待队列
    static Condition conditionProduct = reentrantLock.newCondition();
    //用来表示消费者的等待队列
    static Condition conditionConsumer = reentrantLock.newCondition();

    //用于数据存放的交互容器
    private static List<String> list = new ArrayList<>();
    //用于记录容器的数据量标识
    private static AtomicInteger index = new AtomicInteger(0);
    //最大数据容量
    private static final Integer MAX_NUM = 2;

    public static void main(String[] args) {
        System.out.println("--------------start--------------");
        LockProductConumerTest test = new LockProductConumerTest();
        LockProductConumerTest.Product product = test.new Product();
        product.start();
        LockProductConumerTest.Consumer consumer = test.new Consumer();
        consumer.start();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        //主线程休眠等待
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class Product extends Thread {

        @Override
        public void run() {
            while (true) {
                reentrantLock.lock();
                //当前容器中数据量等于最大数据值时
                while (index.get() >= MAX_NUM) {
                    try {
                        //实际上来说：此处第一次代码执行时的signal()是无效的，因为消费者线程此时本身就是在同步队列中等待唤醒的状态，但是后续消费者节点线程被await()阻塞并移动至等待队列后，此时再执行到该代码 conditionConsumer.signal() 将对应的消费者线程进行唤醒并移动至同步队列中则是有效的；
                        //这个注意事项在 Synchronize中所实现的生产者消费者中同样存在；换句话来说，JDK层面的Lock 和AQS 对于线程锁的操作的实现，实际上就是Synchronize对象锁，wait() 等关键词在JVM层面实现的一种映射；
                        //一个是JVM C 实现的锁，一种是 Java 实现的锁，实现原理基本一致；

                        //唤醒消费者线程，重新将消费者线程转移至同步队列中，进行同步锁的排队抢锁操作
                        conditionConsumer.signal();
                        //阻塞当前生产者线程，当前生产者线程从同步队列移出至conditionProduct的等待队列中，不再进行同步队列的抢锁操作
                        conditionProduct.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //容器内数量不大于 MAX_NUM 则继续新增数据
                String uuid = UUID.randomUUID().toString();
                list.add(uuid);
                //每新增一次数据，index +1
                index.incrementAndGet();
                System.out.println("LockProduct：" + uuid);
                reentrantLock.unlock();
            }
        }
    }

    public class Consumer extends Thread {
        @Override
        public void run() {
            while (true) {
                reentrantLock.lock();
                while (index.get() == 0) {
                    //唤醒生产者线程，使其从conditionProduct的等待队列中移动至同步队列中，重新进行排队抢锁的操作
                    conditionProduct.signal();
                    try {
                        //释放掉消费者线程所持有的同步队列的锁，将其消费者线程移动至conditionConsumer等待队列中，并进入wait等待状态；
                        conditionConsumer.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //获取当前最新的index -1 后的值，对应的则是list容器中的最后一个数据索引位
                Integer listIndex = index.decrementAndGet();
                //消费容器最尾部的数据
                String result = list.get(listIndex);
                //删除该已消费数据,remove(index)
                list.remove(listIndex.intValue());
                System.out.println("LockConsumer：" + result);
                reentrantLock.unlock();
            }
        }
    }
}
