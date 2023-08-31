package com.smart.heart.classloader.re02;

import com.smart.heart.classloader.inface.re02.Phone;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnold.zhao
 * @version IPhone.java, v 0.1 2022-04-25 11:05 Arnold.zhao Exp $$
 */
public class IPhone implements Phone {
    @Override
    public void call() {
        System.out.println("IPhone call in.. test-jar-2");
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");

        for (String v : list) {
            System.out.println(v);
        }

        list.remove(list.size()-1);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        for (String v : list) {
            System.out.println(v);
        }


    }
}
