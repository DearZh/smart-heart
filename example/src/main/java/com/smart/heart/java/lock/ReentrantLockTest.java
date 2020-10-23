package com.smart.heart.java.lock;

import sun.misc.Unsafe;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: ReentrantLock源码解析
 * @Author: Arnold.zhao
 * @Date: 2020/10/21
 */
public class ReentrantLockTest {

    public static void main(String[] args) {


    }

    public static void lock() {
/*

        Unsafe U = Unsafe.getUnsafe();
        System.out.println(U);
*/

        /**
         * Unsafe用于提供操作系统层面的原子操作，内存分配与内存释放，原子对比操作的CAS，线程的挂起与释放等操作
         * 关于Unsafe的介绍:
         * https://www.cnblogs.com/thomas12112406/p/6510787.html
         * https://www.cnblogs.com/linghu-java/p/9686651.html
         * 关于Unsafe.park()挂起线程与object.wait()线程挂起的区别（Java线程等待中Thread.sleep()、Object.wait()、LockSupport.park、UNSAFE.park()的原理与区别）
         * https://blog.csdn.net/weixin_41590779/article/details/107821849
         */


        ReentrantLock reentrantLock = new ReentrantLock();
//        reentrantLock.newCondition()
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程1开始获取锁");
                reentrantLock.lock();
                System.out.println("线程1获取锁成功");
            }
        });
        thread.setName("线程1");
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程2开始获取锁");
                reentrantLock.lock();
                System.out.println("线程2获取锁成功");
            }
        });
        thread1.setName("线程2");

        thread.start();
        thread1.start();
    }

    public static void nodeTest() {
         /*Node node = new Node("node1");
        Node t = new Node("node2");
        node.prev = t;
        t.next = node;

        System.out.println(t);*/
    }

    static class Node {
        private String name;
        public Node prev;
        public Node next;

        public Node(String name) {
            this.name = name;
        }
    }
}
