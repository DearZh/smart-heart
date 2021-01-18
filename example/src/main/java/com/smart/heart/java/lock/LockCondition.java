package com.smart.heart.java.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/16
 */
public class LockCondition {

    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
//        condition.await();
    }

}
