package com.smart.heart.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

/**
 * @Description: ZK相对较为高级的使用特性，主要是集中于分布式环境下的使用方式等
 * @Author: Arnold.zhao
 * @Date: 2020/8/12
 */
public class SeniorTest {
    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = ZKClient.instance();
//        transaction(curatorFramework);
//        watch1(curatorFramework);
        watch2(curatorFramework);
    }

    /**
     * 分布式锁的实现，
     * master选举，
     * 分布式协调，通知
     * https://blog.csdn.net/tang06211015/article/details/51850516?locationNum=9&fps=1
     */

    //**********************************************************************************
    //**************ZK有好几种不同事件的监听**************************************
    //**************https://blog.csdn.net/mlljava1111/article/details/79429079*****
    //**************https://blog.csdn.net/m0_46201444/article/details/107785276******
    public static void watch2(CuratorFramework curatorFramework) throws Exception {
        /**
         * 目前还不清楚，具体使用方式，及使用场景，不过这个监听的方式 curatorEvent可以获取到监听的状态等信息
         */
        String path = "/init-path-value-null1";
        CuratorListener curatorListener = new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println(curatorEvent.getName());
                System.out.println(curatorEvent.getType().name());
                System.out.println(curatorEvent.getType().toString());
                System.out.println(curatorEvent.getWatchedEvent().getType());
                System.out.println(curatorEvent.getWatchedEvent().getWrapper().getState());
            }
        };
        curatorFramework.getCuratorListenable().addListener(curatorListener);
        curatorFramework.getData().inBackground().forPath(path);
        curatorFramework.setData().forPath(path, "内容".getBytes());
        Thread.sleep(600000);
    }

    /**
     * 节点监听，只要触发节点的内容修改，或者节点删除，或者新增了该节点，则都会触发该监听；
     * 可以在监听器启动的时候，并没有该节点，后续只要该节点被新增了，则就可以触发到该监听；
     *
     * @param curatorFramework
     * @throws Exception
     */
    public static void watch1(CuratorFramework curatorFramework) throws Exception {
        /**
         * 不太好的一点是，这种监听方式，不能获取到的变更的状态；
         */
        String path = "/init-path-value-null1";
        NodeCache nodeCache = new NodeCache(curatorFramework, path);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {

                System.out.println("接受到监听事件,路径为：" + nodeCache.getCurrentData().getPath() + "数据变更为：" + new String(nodeCache.getCurrentData().getData()));
            }
        });
        nodeCache.start();
        Thread.sleep(900000);
    }

    //**********************************************************************
    //****************************事务操作**********************************
    //**********************************************************************


    /**
     * ZK的事务操作<br/>
     * ZK的每一个CRUD操作都是原子操作，一系列的原子操作合并后就是一个事务
     */
    public static void transaction(CuratorFramework curatorFramework) throws Exception {
        String path = "/transaction";
        curatorFramework.inTransaction()
                .create().forPath(path, "新建事务内容".getBytes()).and()
                .setData().forPath(path, "修改事务内容".getBytes())
                .and().commit();
    }
}
