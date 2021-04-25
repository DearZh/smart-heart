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

    public static void main(String[] args) {
        //TODO:1021 通过Cpu核数来确定线程量 Arnold.zhao 2020/8/10
        int threadCount = Runtime.getRuntime().availableProcessors();
        System.out.println(threadCount);
    }
}
