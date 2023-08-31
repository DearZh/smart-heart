package com.smart.heart.java.thread;

/**
 * @author Arnold.zhao
 * @version Test.java, v 0.1 2022-02-14 17:36 Arnold.zhao Exp $$
 */
public class Test {
    public static void main(String[] args) {
        Thread t1 = new MyCommon();
//        Thread t2 =  new Thread( new MyDaemon());
//        t2.setDaemon( true); //设置为守护线程
//        t2.start();
//        t1.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 999999999; i++) {
                    System.out.println("线程1第" + i + "次执行！");
                   /* try {
                        Thread.sleep(7);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        },"arnold-test-thread").start();
    }

    static class MyCommon extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println("线程1第" + i + "次执行！");
                try {
                    Thread.sleep(7);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
