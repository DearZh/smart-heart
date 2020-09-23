/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/9/21
 */
package com.smart.heart.zookeeper.leader.latch;
/**
 * 采用LeaderLatch的方式来进行选举时，节点在加入选举以后，除非程序结束或者close()退出选举，否则节点自加入选举以后将持续持有或者保持对主节点的竞争。
 * 另外，采用LeadLatch进行选举时，当前节点close()以后，则不会再参与节点的竞争，这些方面与LeaderSelector还是存在区别的；
 * blogs：
 * https://www.cnblogs.com/lay2017/p/10274872.html
 * https://blog.csdn.net/hosaos/article/details/88727817
 **/