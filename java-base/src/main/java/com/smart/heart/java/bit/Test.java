package com.smart.heart.java.bit;

import java.util.HashMap;

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
