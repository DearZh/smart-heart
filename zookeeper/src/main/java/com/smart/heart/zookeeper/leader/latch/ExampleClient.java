package com.smart.heart.zookeeper.leader.latch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/9/21
 */
public class ExampleClient {
    private String name;
    private CuratorFramework curatorFramework;
    private String path;
    private LeaderLatch leaderLatch;

    public ExampleClient(String name, String path, CuratorFramework curatorFramework) {
        this.name = name;
        this.path = path;
        this.curatorFramework = curatorFramework;
        leaderLatch = new LeaderLatch(curatorFramework, path, name, LeaderLatch.CloseMode.SILENT);
        leaderLatch.addListener(new LeaderLatchListener() {
            @Override
            public void isLeader() {
                System.out.println(name + " 竞争主节点成功");
                try {
                    Thread.sleep(5000);
//                    leaderLatch.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notLeader() {
                System.out.println(name + " 竞争主节点失败");
            }
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void start() throws Exception {

        leaderLatch.start();
    }

    public void close() throws Exception {

        leaderLatch.close();
    }

    public boolean hasLeadership() {
        /*try {
            System.out.println(leaderLatch.getLeader().isLeader() + ">>>" + leaderLatch.hasLeadership());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return leaderLatch.hasLeadership();
    }
}
