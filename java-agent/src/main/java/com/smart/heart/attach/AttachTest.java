package com.smart.heart.attach;

/**
 * @author Arnold.zhao
 * @version AttachTest.java, v 0.1 2022-05-01 21:31 Arnold.zhao Exp $$
 */
public class AttachTest {
    public static void main(String[] args) {
        while (true) {
            System.out.println(foo());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int foo() {
        return 100;
    }
}
