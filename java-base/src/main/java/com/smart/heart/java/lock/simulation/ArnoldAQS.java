package com.smart.heart.java.lock.simulation;

/**
 * @Description: AQS = Abstract Queued Synchronize
 * @Author: Arnold.zhao
 * @Date: 2021/1/18
 */
public abstract class ArnoldAQS {

    Node head;
    Node tail;

    class Node {

        private Node prev;
        private Node next;
        private Thread thread;

        private Integer waiterState;

        private final Integer CANCELLED = 1;    //表示当前节点所对应线程被取消
        private final Integer SIGNAL = -1;      //表示后节点线程待释放
        private final Integer CONDITION = -2;   //表示线程正在等待条件

        public Node getPrev() {

            return this.prev;
        }
    }

    public void acquire() {
        if (!tryAcquire()) {

        }
    }

    /**
     * 判断是否可以获取锁
     *
     * @return
     */
    protected boolean tryAcquire() {
        throw new UnsupportedOperationException();
    }
}
