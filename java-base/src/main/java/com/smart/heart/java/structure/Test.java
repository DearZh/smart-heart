package com.smart.heart.java.structure;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/2/25
 */
public class Test {

    public static void main(String[] args) {



//        new ArrayList().add()
        LinkedList linkedList = new LinkedList();
        linkedList.add("");

        Queue queue12 = new LinkedList();
        queue12.add("");
        ((LinkedList) queue12).remove(2);

//        TreeSet


        new ArrayList<>().add("");
        Stack stack = new Stack();
        stack.push("");
        stack.peek();
        Queue queue = new ConcurrentLinkedQueue();
        ConcurrentLinkedQueue queue1 = new ConcurrentLinkedQueue();
        new ArrayBlockingQueue<String>(22);
    }
}
