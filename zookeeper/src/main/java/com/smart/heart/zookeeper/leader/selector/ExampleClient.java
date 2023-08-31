package com.smart.heart.zookeeper.leader.selector;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 基于LeaderSelector实现leader选举
 * 利用Curator中InterProcessMutex分布式锁进行抢主，抢到锁的即为Leader
 * @Author: Arnold.zhao
 * @Date: 2020/9/21
 */
public class ExampleClient extends LeaderSelectorListenerAdapter implements Closeable {

    /**
     * 采用LeaderSelector的方式来实现节点选举时，主节点选举成功，将会执行takeLeadership代码块，当该代码块执行完成后，则会放弃主节点，然后由剩下的节点再次进行主节点竞争
     */

    private final LeaderSelector leaderSelector;
    private final String name;
    private final AtomicInteger leaderCount = new AtomicInteger();

    public ExampleClient(CuratorFramework client, String path, String name) {
        this.name = name;
        this.leaderSelector = new LeaderSelector(client, path, this);
        //在大多数情况下，当前实例放弃领导权时，则希望实例重新排队进行主节点的竞争
        leaderSelector.autoRequeue();
        System.out.println("*********ExampleClient***********");
    }


    public void start() {
        leaderSelector.start();
    }

    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        // 被选择为主节点后，将会执行该takeLeadership代码块，当该代码块执行完成后，则会放弃主节点，然后由剩下的节点再次进行主节点竞争

        final int waitSeconds = (int) (5 * Math.random()) + 1;

        System.out.println(name + " is now the leader. Waiting " + waitSeconds + " seconds...");
        System.out.println(name + " has been leader " + leaderCount.getAndIncrement() + " time(s) before.");
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(waitSeconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println(name + " was interrupted.");
            Thread.currentThread().interrupt();
        } finally {
            System.out.println(name + " relinquishing leadership.\n");
        }
    }

    @Override
    public void close() throws IOException {
        leaderSelector.close();
    }
}
