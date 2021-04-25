package com.smart.heart.java.basic;

import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Arnold.zhao
 * @create 2021-04-09
 */
public class QueueTest {
    public static void main(String[] args) throws InterruptedException {
        /*noPriorityQueue();
        System.out.println(">>>>>>>>>>>>>>>");
        noWaitLinkedBlockingQueue();
        */
        noWaitConcurrentLinkedQueue();
    }

    public static void noWait() {
        //互为主备的两个Queue队列；
        Integer masterQueue = 10000;
        Integer bakQueue = 1000;
        LinkedBlockingQueue<String> blockingQueue = new LinkedBlockingQueue(masterQueue);
        System.out.println(blockingQueue.remainingCapacity());
        //原始队列 masterQueue 1000条数据，初始化一个 Queue
        //扩展队列数据，此时扩展到1W条；则1W - 1K > 0 则表示是新增队列数，此时新增一个bakQueue大小为 9000；
        //再次更改队列数据，此时扩展到10W；则原始 masterQueue 数据全部消费完毕，则重新实例化 masterQueue 大小为 9W；

        //

    }

    class TestPriorityQueue<E> extends PriorityQueue<E> {
        /*@Override
        public boolean offer(E e) {
            if (e == null)
                throw new NullPointerException();
            modCount++;
            int i = size;
            if (i >= queue.length)
                grow(i + 1);
            size = i + 1;
            if (i == 0)
                queue[0] = e;
            else
                siftUp(i, e);
            return true;
        }*/

        /*@Override
        public boolean offer(E e) {
            if (e == null) {
                throw new NullPointerException();
            }
            super.modCount++;


            return false;
        }*/
    }

    public static void noPriorityQueue() {
        PriorityQueue blockingQueue1 = new PriorityQueue(1);
        boolean flag = blockingQueue1.offer("C");
        System.out.println("C插入结果" + flag + "size：" + blockingQueue1.size());
        flag = blockingQueue1.offer("B");
        System.out.println("B插入结果" + flag + "size：" + blockingQueue1.size());
        flag = blockingQueue1.offer("A");
        System.out.println("A插入结果" + flag + "size：" + blockingQueue1.size());

        System.out.println(blockingQueue1.poll() + "size：" + blockingQueue1.size());
        System.out.println(blockingQueue1.poll() + "size：" + blockingQueue1.size());
        System.out.println(blockingQueue1.poll() + "size：" + blockingQueue1.size());
        System.out.println(blockingQueue1.poll() + "size：" + blockingQueue1.size());
    }

    public static void noWaitConcurrentLinkedQueue() {
        ConcurrentLinkedQueue blockingQueue1 = new ConcurrentLinkedQueue();
        boolean flag = blockingQueue1.offer("A");
        System.out.println("A插入结果" + flag);
        flag = blockingQueue1.offer("B");
        flag = blockingQueue1.offer("B");
        System.out.println("B插入结果" + flag);
        flag = blockingQueue1.offer("C");
        System.out.println("C插入结果" + flag);

        System.out.println(blockingQueue1.poll());
        System.out.println(blockingQueue1.poll());
        System.out.println(blockingQueue1.poll());
        System.out.println(blockingQueue1.poll());
        System.out.println(blockingQueue1.poll());
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            blockingQueue1.offer(i);
        }
        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(">>>>>");
        startTime = System.currentTimeMillis();
        System.out.println(blockingQueue1.size());
        System.out.println(System.currentTimeMillis() - startTime);

    }

    public static void noWaitLinkedBlockingQueue() throws InterruptedException {
        LinkedBlockingQueue<String> blockingQueue1 = new LinkedBlockingQueue(2);
        boolean flag = blockingQueue1.offer("A");
        System.out.println("A插入结果" + flag);
        flag = blockingQueue1.offer("B");
        System.out.println("B插入结果" + flag);
        flag = blockingQueue1.offer("C");
        System.out.println("C插入结果" + flag);
        System.out.println(blockingQueue1.poll());
        System.out.println(blockingQueue1.poll());
        System.out.println(blockingQueue1.poll());
        System.out.println(blockingQueue1.poll());

    }

    public static void noWaitArrayBlockingQueue() throws InterruptedException {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(2);

        Thread thread = new Thread(() -> {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean flag = blockingQueue.offer("C");
            System.out.println("C插入结果：" + flag);
            flag = blockingQueue.offer("D");
            System.out.println("D插入结果：" + flag);
            flag = blockingQueue.offer("E");
            System.out.println("E插入结果：" + flag);
        });
        blockingQueue.remainingCapacity();

        thread.start();
        Thread.sleep(6000);
        String result = blockingQueue.poll();
        System.out.println("消费结果1：" + result);
        result = blockingQueue.poll();
        System.out.println("消费结果2：" + result);
        result = blockingQueue.poll();
        System.out.println("消费结果3：" + result);
    }

    public static void wait1() throws InterruptedException {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(2);
        Thread thread = new Thread(() -> {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                blockingQueue.put("C");
                blockingQueue.put("D");
                blockingQueue.put("E");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        System.out.println("阻塞");
        System.out.println(blockingQueue.take());
        ;
        System.out.println("消费");
        System.out.println("------------");
        blockingQueue.put("A");

        /*
        System.out.println(blockingQueue.offer("A"));
        System.out.println(blockingQueue.contains("A"));*/
        System.out.println(blockingQueue.take());
        System.out.println(">>>>>>>>>");
        System.out.println(blockingQueue.take());
        System.out.println(">>>>>>>>>>>>>>>>>>");
    }
}
