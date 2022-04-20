package com.smart.heart.java.lock.thread.test;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/12/8
 */
public class Test {

    private int a = 3;

    public static void main(String[] args) throws InterruptedException {
        Test test = new Test();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    test.a = 4;
                    System.out.println("1 thread：" + test.a);
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    test.a = 5;
                    System.out.println("2 thread：" + test.a);
                }
            }
        }).start();

        Thread.sleep(10000);
        System.out.println("-------- end ------");
    }
}
