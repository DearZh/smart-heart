package com.smart.heart.java.lock.thread;

/**
 * @Description: 线程阻塞的相关方式
 * @Author: Arnold.zhao
 * @Date: 2020/10/23
 */
public class ThreadWaitTest {

    public static void main(String[] args) {
        //
        ThreadWaitTest waitTest = new ThreadWaitTest();
        try {
            //必须获取到对象锁时才可以进入等待状态，否则会抛出java.lang.IllegalMonitorStateException 异常。
            //等待期间，会释放所持有的锁
            //线程状态为WAITING表示一直等待，通过Object.notify()唤醒。
            waitTest.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            //等待期间，不会释放所持有的锁
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
