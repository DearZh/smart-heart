package com.smart.heart.java;

import sun.misc.Unsafe;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/10/14
 */
public class JavaBasicTest {
    public static void main(String[] args) {

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
