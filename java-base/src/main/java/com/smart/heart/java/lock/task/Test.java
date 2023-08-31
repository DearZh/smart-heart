package com.smart.heart.java.lock.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author Arnold.zhao
 * @version Test.java, v 0.1 2022-06-09 14:21 Arnold.zhao Exp $$
 */
public class Test {

    public static void main(String[] args) throws Exception {

        FutureTask<String> future = new FutureTask<>(new Execute());
        Thread thread = new Thread(future);
        thread.start();

        Thread.sleep(1000);
        System.out.println(future.isDone() ? future.get() : "error");
    }

    static class Execute implements Callable<String> {
        @Override
        public String call() throws Exception {
            return "success";
        }
    }
}
