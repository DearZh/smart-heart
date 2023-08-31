package com.smart.heart.java.bit;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Arnold.zhao
 * @version Test.java, v 0.1 2022-03-21 17:32 Arnold.zhao Exp $$
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(3 | -5);
        //0000 0011 | 1111 1011 =1111 1011
        //0000 0011 & 1111 1011 =0000 0011
        System.out.println(3 & 5);
        System.out.println(3 & 6);
        System.out.println(">>>>>>>>>>");
        System.out.println(174 & 15);
        System.out.println(Integer.toBinaryString(-567));
        System.out.println(Long.toBinaryString(-567));
        //交换两个数
        System.out.println("交换两个数");
        int a = 2;
        int b = -2;
        System.out.println("原始数据：\n" + Integer.toBinaryString(a) + "\n" + Integer.toBinaryString(b) + "\n");
        a = a ^ b;
        System.out.println("第一次交换：\n" + Integer.toBinaryString(a) + "\n" + Integer.toBinaryString(b) + "\n");
        b = a ^ b;
        System.out.println("第二次交换：\n" + Integer.toBinaryString(a) + "\n" + Integer.toBinaryString(b) + "\n");
        a = a ^ b;
        System.out.println("第三次交换：\n" + Integer.toBinaryString(a) + "\n" + Integer.toBinaryString(b) + "\n");
        System.out.println(a);
        System.out.println("交换完成");
        System.out.println("A=" + a + " b=" + b);
        System.out.println(">>>>>>>>>>>>>>>>>>>");
        swap(2, 4);
        swapBit(10, 30);
        System.out.println(">>>>>>左移运算");
        System.out.println(Integer.toBinaryString(16));
        System.out.println(Integer.toBinaryString(-16));
        System.out.println(16 >> 2);
        System.out.println(-16 >> 2);
        System.out.println(Integer.toBinaryString(-4));
        System.out.println(12 >> 2);
        System.out.println(Long.toBinaryString(-23333333333333L));
        byte b1 = 2;
        int b2 = b1 & 2;
        System.out.println(">>>>>>>>>>>>>>>>>>>");
        System.out.println(Integer.toBinaryString(10));//1010
        System.out.println(Integer.toBinaryString(-10));//11111111111111111111111111111101
        System.out.println(Long.toBinaryString(10));//1010
        System.out.println(Long.toBinaryString(-10));//1111111111111111111111111111111111111111111111111111111111110110
        System.out.println(">>>>>>>>>>一个数提取出两个值>>>>>>>>>>>>>>");
        //& 与运算符
        //运算规则：两位同时为1，结果才为1，否则结果为0。

        System.out.println("COUNT_BITS:" + COUNT_BITS);
        System.out.println("CAPACITY:" + CAPACITY);
        System.out.println("RUNNING:" + RUNNING);
        System.out.println("SHUTDOWN:" + SHUTDOWN);
        System.out.println("STOP:" + STOP);
        System.out.println("TIDYING:" + TIDYING);
        System.out.println("TERMINATED:" + TERMINATED);
        System.out.println(ctl);
        System.out.println(">>>>>>>>>>>>>>");
        System.out.println(runStateOf(5));
        System.out.println(workerCountOf(5));


        System.out.println(Integer.toBinaryString(-1));
        Integer ac = 2130504 & 2396160;
        System.out.println(ac);
        System.out.println(Integer.toBinaryString(ac));
        /*11111111111111111111111111111111
        00000000000000000000000011110000
                */
    }


    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING = -1 << COUNT_BITS;
    private static final int SHUTDOWN = 0 << COUNT_BITS;
    private static final int STOP = 1 << COUNT_BITS;
    private static final int TIDYING = 2 << COUNT_BITS;
    private static final int TERMINATED = 3 << COUNT_BITS;

    private static final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    // Packing and unpacking ctl
    private static int runStateOf(int c) {
        return c & ~CAPACITY;
    }

    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }


    static void swap(int a, int b) {
        final String s = new String("");
        final String d = new String("D");
        //introduce local variable
        //declare
        int c = a;
        a = b;
        b = c;
    }

    static void swapBit(int a, int b) {
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
    }
}
