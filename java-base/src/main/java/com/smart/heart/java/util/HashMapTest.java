package com.smart.heart.java.util;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Arnold.zhao
 * @version HashMapTest.java, v 0.1 2022-03-30 19:43 Arnold.zhao Exp $$
 */
public class HashMapTest {

    public static void main(String[] args) {

        HashMap h = new HashMap();
        h.put("", "");
        System.out.println("a".hashCode());
        System.out.println(new Object().hashCode());
        Integer a = 2;
        System.out.println(a.hashCode());

        h.put(null, "abc");
//        ConcurrentHashMap
        System.out.println(h.get(null));

        System.out.println(hash(null));
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
