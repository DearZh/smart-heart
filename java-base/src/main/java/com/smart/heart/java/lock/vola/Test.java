package com.smart.heart.java.lock.vola;

/**
 * @author Arnold.zhao
 * @version Test.java, v 0.1 2022-06-09 10:37 Arnold.zhao Exp $$
 */
public class Test {
    volatile int i;

    public void addI() {
        i++;
        //等价于 i= i +1; 等价于 int a = i+1; i = a;
        //i++ 等价于两个操作：int a = i+1; i = a; 但 i=a 是可以保证原子性，并同步到主内存的，但是int a = a+1;并不是线程安全的，
        // 所以最终由于int a的值并非线程安全，最终得到的a的值也并不准确，所以最终赋值后的i的值也将是错误的值。
    }

    //https://www.cnblogs.com/paddix/p/5428507.html
    public static void main(String[] args) throws InterruptedException {
        final Test test01 = new Test();
        for (int n = 0; n < 1000; n++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    test01.addI();
                }
            }).start();
        }

        Thread.sleep(10000);//等待10秒，保证上面程序执行完成

        System.out.println(test01.i);
    }
}
