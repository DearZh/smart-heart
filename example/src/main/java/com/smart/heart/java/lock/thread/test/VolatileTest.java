package com.smart.heart.java.lock.thread.test;


public class VolatileTest {
    int a = 1;
    int b = 2;

    public void change() {
        a = 3;
        System.out.println("test");
        b = a;
        System.out.println("执行完成");
    }

    public void print() {
        String result = "b=" + b + ";a=" + a;
        if (result.equals("b=2;a=1") || result.equals("b=3;a=3")) {
            return;
        }
        System.out.println(result);
    }

    public static void main(String[] args) {
        while (true) {
            final VolatileTest test = new VolatileTest();
            new Thread(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.change();
            }).start();

            new Thread(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.print();
            }).start();
        }
    }
}