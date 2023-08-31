package com.smart.heart.java.lock.thread.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Arnold.zhao
 * @version FutureTest.java, v 0.1 2021-05-07 17:26 Arnold.zhao Exp $$
 */
public class FutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println();
                return null;
            }
        });
        futureTask.run();
        futureTask.get();
    }
}
