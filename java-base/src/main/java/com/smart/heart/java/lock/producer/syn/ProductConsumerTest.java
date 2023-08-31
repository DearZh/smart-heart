package com.smart.heart.java.lock.producer.syn;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 注意：此处实现的生产者消费者模式为：生产者所产生数据到指定条数后，消费者进行消费，消费完以后，生产者重新进行数据生产；
 * <br/>而不是生产者实时生产，消费者实时消费的模式；
 * @Author: Arnold.zhao
 * @Date: 2021/1/28
 */
public class ProductConsumerTest {
    //用于数据存放的交互容器
    private static List<String> list = new ArrayList<>();
    //用于记录容器的数据量标识
    private static AtomicInteger index = new AtomicInteger(0);
    //最大数据容量
    private static final Integer MAX_NUM = 5;


    public class Product extends Thread {

        @Override
        public void run() {
            while (true) {
                synchronized (list) {
                    while (index.get() >= MAX_NUM) {
                        try {
                            //唤醒消费者线程进行消费
                            list.notifyAll();
                            //释放掉当前生产者线程所持有的 list容器的锁，并阻塞该生产者线程
                            list.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //容器内数量不大于 MAX_NUM 则继续新增数据
                    String uuid = UUID.randomUUID().toString();
                    list.add(uuid);
                    //每新增一次数据，index +1
                    index.incrementAndGet();
                    System.out.println("Product：" + uuid);
                }
            }
        }
    }

    public class Consumer extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (list) {
                    while (index.get() == 0) {
                        //容器内数据消费为空了，则唤醒生产者继续生产数据
                        list.notifyAll();
                        try {
                            //释放掉消费者线程所持有的list容器的锁
                            list.wait();
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
                    System.out.println("Consumer：" + result);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--------------start--------------");
        ProductConsumerTest test = new ProductConsumerTest();
        Product product = test.new Product();
        product.start();
        Consumer consumer = test.new Consumer();
        consumer.start();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        //主线程休眠等待
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * Product：a4140134-1b63-4a7d-93b2-2d7bff369acf
         * Product：646b798b-822d-44ea-a1dd-d18d2fe44d97
         * Product：4fcada04-5ede-422b-80bb-798621d67afc
         * Product：26c78008-685d-4a98-8307-2fab9f1cd0ae
         * Product：c6165237-4fa9-4cd0-9b0e-7d0917bb6672
         * Consumer：c6165237-4fa9-4cd0-9b0e-7d0917bb6672
         * Consumer：26c78008-685d-4a98-8307-2fab9f1cd0ae
         * Consumer：4fcada04-5ede-422b-80bb-798621d67afc
         * Consumer：646b798b-822d-44ea-a1dd-d18d2fe44d97
         * Consumer：a4140134-1b63-4a7d-93b2-2d7bff369acf
         * 测试效果如上：消费者每次消费的第一条数据均为生产者所生产的最后一条数据；先进后出
         */
    }
}
