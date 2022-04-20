package com.smart.heart.java.basic;

import java.util.PriorityQueue;

/**
 * 互为主备队列，便于队列容量变更，无阻塞 <br/>
 * 现有的jdk队列同时支持：容量变更，无阻塞，超出容量大小则丢弃的队列暂无；<br/>
 *
 * @author Arnold.zhao
 * @create 2021-04-09
 */
public class MasterStandbyQueue<E> {

    //场景：kafka 消费失败率较高，此时扩展队列大小由原先的10w，扩展至 100W；用于存储更多的失败数据进行后续补偿消费；
    //kafka消费失败率降低，此时调低队列大小？避免存储太多的失败数据？导致OOM？ 队列本身就是在被消费过程中的，如果重新队列中增加了过多的数据，那说明还是有问题的，而且
    //基于队列做缓冲的需求，如果消费插入ES存在较多的异常，则队列无论多大都没有任何作用，所以，默认初始化一个队列大小即可。初始化这个值需通过调优解决。

    Integer masterCapacity = 5000;

    Integer standbyCapacity = 0;

    PriorityQueue<E> masterQueue = new PriorityQueue(masterCapacity);
    PriorityQueue<E> standbyQueue = new PriorityQueue(standbyCapacity);

    /**
     * lion change listening
     */
    public void change(Integer newCapacity) {

        //容量大小变更，当前为新增队列大小
        if (newCapacity > masterCapacity) {
            //判断standbyQueue中是否存在数据

            //当前已使用的队列长度为空，则扩展该队列为新的队列容量
            if (standbyQueue.size() == 0) {
                Integer changeCapacity = newCapacity - masterCapacity;
                standbyQueue = new PriorityQueue<>(changeCapacity);
            } else if (standbyQueue.size() > 0) {
                //当前队列中存在数据，则判断两队列大小，

            }


        } else if (newCapacity < masterCapacity) {
            //缩小容量大小

        }
    }

    public boolean offer(E e) {
        return masterQueue.offer(e);
    }

    public E poll() {
        return masterQueue.poll();
    }

    public static void main(String[] args) {

    }

}
