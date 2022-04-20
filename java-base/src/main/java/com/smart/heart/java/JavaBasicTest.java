package com.smart.heart.java;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.function.IntSupplier;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/10/14
 */
public class JavaBasicTest {

    volatile JavaBasicTest javaBasicTest = null;

    public static void main(String[] args) {

        LinkedList linkedList = new LinkedList();
        linkedList.add("");
        new ArrayList<>().add("");
        Stack stack = new Stack();
        stack.push("");
        stack.peek();
        Queue queue = new ConcurrentLinkedQueue();
        ConcurrentLinkedQueue queue1 = new ConcurrentLinkedQueue();



        /*int i = 0;
        while (true) {
            i++;
            System.out.println(i);
            if (i > 10) {
                break;
            } else {
                return;
            }
        }

        System.out.println("DD");
        */
/*
        ad(v -> {

            System.out.println("**");
            System.out.println(v);
            return null;
        });*/


        /*adc(() -> {

            return 1;
        });
        */
    }

    public static void adc(IntSupplier function) {

    }

    public static void ad(Function function) {
        function.apply(null);
    }


}
