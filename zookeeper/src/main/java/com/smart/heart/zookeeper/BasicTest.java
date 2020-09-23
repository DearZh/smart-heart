package com.smart.heart.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @Description: ZK的基础使用特性
 * @Author: Arnold.zhao
 * @Date: 2020/8/11
 */
public class BasicTest {

    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = ZKClient.instance();

        /** 基础用法 */
        basic(curatorFramework);
    }

    /**
     * ZK 增删改查每一个节点时都是原子操作；<br/>
     * 拿新增节点举例：新增一个节点时，如果节点已经存在，那么二次新增该节点时将会抛出异常，在分布式的集群环境下，同时有多个客户端进行节点创建的操作，最终则只能有一个客户端创建成功，那么利用这个特性，则
     * 可以轻易的在分布式环境中进行集群选举了；
     *
     * @param curatorFramework
     * @throws Exception
     */
    public static void basic(CuratorFramework curatorFramework) throws Exception {

        String path = "/init-path-value-null1";
        if (!checkExists(curatorFramework, path)) {
            //路径节点不存在则新增该节点
            String result = curatorFramework.create().forPath(path, new byte[0]);

            /**
             * 默认直接不指定内容创建空数据时，zk中的value会存储当前机器的ip地址
             * curatorFramework.create().forPath(path);
             */
            //返回的result结果，即为创建时所指定的路径
            System.out.println("路径节点新增：" + result);
        } else {
            //查询数据节点内容
            byte[] content = curatorFramework.getData().forPath(path);
            System.out.println("路径节点查询：" + new String(content));
        }

        //创建一个包含内容的节点
        String pathContent = "/init-path-value";
        if (!checkExists(curatorFramework, pathContent)) {
            //路径节点不存在则新增该节点
            String result = curatorFramework.create().forPath(pathContent, "内容".getBytes());
            System.out.println("内容节点新增：" + result);
        } else {
            //查询数据节点内容，包含状态的查询； 》》》 这里状态查询，设置版本后查询好像并没有什么效果，查询的结果每次都是最新的版本的结果
            Stat stat = new Stat();
            stat.setVersion(2);
            byte[] content = curatorFramework.getData().storingStatIn(stat).forPath(pathContent);
            System.out.println("内容节点查询：" + new String(content));
        }

        String whilePath = "/init/pathvalue/node1";
        Stat whileStat = curatorFramework.checkExists().creatingParentContainersIfNeeded().forPath(whilePath);
        if (whileStat == null) {
            /**
             * CreateMode类型分为4种
             * 1.PERSISTENT--持久型
             * 2.PERSISTENT_SEQUENTIAL--持久顺序型 （比如创建node1的节点，使用持久顺序型，则第一次创建成功后节点key名称则是：node1 0000000001，当第二次还是进行创建操作时，则key名会修改为node1 0000000002，数字依次递增，且每次都是在原有的key上进行修改）
             * 3.EPHEMERAL--临时型
             * 4.EPHEMERAL_SEQUENTIAL--临时顺序型
             * 临时节点贮存在zookeeper中，当连接和session断掉后则该节点会被删除
             */
            //创建临时节点，并递归创建父节点，在递归创建父节点时，父节点为持久节点
            String result = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(whilePath);
            System.out.println("递归节点创建：" + result);
        } else {
            byte[] content = curatorFramework.getData().forPath(whilePath);
            System.out.println("递归节点查询：" + new String(content));
        }

        /**
         * 更新数据，节点的每次修改，version版本都会递增
         */
        Stat stat = curatorFramework.setData().forPath(pathContent, "新内容4".getBytes());
        System.out.println(pathContent + "节点更新：" + stat.getVersion());
        //指定版本更新，如果当前版本不一致则更新时将会抛出异常，只有版本一致才会更新
        stat = curatorFramework.setData().withVersion(stat.getVersion()).forPath(pathContent, "指定版本更新".getBytes());
        //节点的每次修改，version版本都会递增
        System.out.println(pathContent + "节点指定版本更新：" + stat.getVersion());

        /**
         * 删除节点
         */
        curatorFramework.delete().forPath(path);
        //删除节点并递归删除其子节点 》》》 删除 /init下的所有子节点，包含自身init节点。如果是指定的/init/pathvalue路径，则表示删除/init/pathvalue下的所有子节点，包括pathvalue节点
        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/init");
        //指定版本进行删除 >>>> 你懂的，指定指定版本进行的操作，其实就是一个乐观锁的操作，可以相对友好的控制并发行为，，如果当前指定的version不存在，则抛出异常；
//        curatorFramework.delete().withVersion(42).forPath(pathContent);
        //强制保证删除一个节点；>>>> 只要客户端会话还有效，那么curator就会在后台持续的进行删除操作，直到节点删除成功。比如遇到网络异常等情况，此时的guaranteed()的强制删除就会很有效果
        curatorFramework.delete().guaranteed().forPath(pathContent);
    }

    /**
     * 检查指定的路径节点是否存在，存在则执行true，不存在则返回false
     *
     * @return
     * @throws Exception
     */
    public static boolean checkExists(CuratorFramework curatorFramework, String path) throws Exception {

        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat != null) {
            return true;
        } else {
            return false;
        }
    }
}
