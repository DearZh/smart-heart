package com.smart.heart.java.thread.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadFactory
 *
 * @author Arnold.zhao <a href="mailto:Arnold_zhao@126.com"/>
 * @create 2021-04-06
 */
public class Test {
    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("callSessionWs-pool-%d").build();

    private static ExecutorService pool = new ThreadPoolExecutor(8, 8,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(50), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 记位数
     */
    private static final int COUNT_BITS = Integer.SIZE - 3;
    /**
     * 容量
     */
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    public static void main(String[] args) {
        //TODO:1021 通过Cpu核数来确定线程量 Arnold.zhao 2020/8/10
        /*int threadCount = Runtime.getRuntime().availableProcessors();
        System.out.println(threadCount);
        */
//        pool.submit();
        int RUNNING = -1 << COUNT_BITS;
        int ctl = ctlOf(RUNNING, 0);

        int SHUTDOWN = 0 << COUNT_BITS;
        int STOP = 1 << COUNT_BITS;
        int TIDYING = 2 << COUNT_BITS;
        int TERMINATED = 3 << COUNT_BITS;
        System.out.println("记位数：" + COUNT_BITS);
        System.out.println("CAPACITY容量：" + CAPACITY);

        System.out.println("RUNNING：" + RUNNING);
        System.out.println("SHUTDOWN：" + SHUTDOWN);
        System.out.println("STOP：" + STOP);
        System.out.println("TIDYING：" + TIDYING);
        System.out.println("TERMINATED：" + TERMINATED);
        System.out.println("ctl运行状态：" + runStateOf(ctl));
        System.out.println("线程数量：" + workerCountOf(ctl));
    }

    /**
     * 线程数
     *
     * @param c
     * @return
     */
    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    /**
     * 计算当前运行状态
     *
     * @param c
     * @return
     */
    private static int runStateOf(int c) {
        return c & ~CAPACITY;
    }

    /**
     * 通过状态和线程数生成ctl
     *
     * @param rs
     * @param wc
     * @return
     */
    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }
}
