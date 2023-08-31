package com.smart.heat.netty.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Arnold.zhao <a href="mailto:Arnold_zhao@126.com"/>
 * @create 2021-03-27
 */
public class Test01 {

    protected static Map<Integer, String> sectionMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        Map<Integer, Integer> itemMap = new ConcurrentHashMap<>();

        Integer v = itemMap.put(1, 2);
        System.out.println("V=" + v);
        System.out.println("Map=" + itemMap);
        v = itemMap.put(1, 3);
        System.out.println("V=" + v);
        System.out.println("Map=" + itemMap);


        sectionMap.computeIfAbsent(1, String::valueOf);
        System.out.println(sectionMap);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        int[] a = new int[10];
        a[0] = 80;
        a[1] = 89;
        a[2] = 90;
        a[3] = 99;
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

}
