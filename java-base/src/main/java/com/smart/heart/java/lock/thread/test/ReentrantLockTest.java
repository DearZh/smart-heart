package com.smart.heart.java.lock.thread.test;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: ReentrantLock源码解析
 * @Author: Arnold.zhao
 * @Date: 2020/10/21
 */
public class ReentrantLockTest {

    public static void main(String[] args) throws InterruptedException {

//
        // 高16位为读锁，低16位为写锁
         final int SHARED_SHIFT   = 16;
        // 读锁单位
         final int SHARED_UNIT    = (1 << SHARED_SHIFT);
        // 读锁最大数量
         final int MAX_COUNT      = (1 << SHARED_SHIFT) - 1;
        //
         System.out.println(SHARED_UNIT);
        System.out.println(MAX_COUNT);



        /*ReentrantLock r
                 = new ReentrantLock();
        r.newCondition()
                */
        /*List<String> list =  new ArrayList();
        list.add()
                */
        //AQS,ReentrantLock,CountDownLatch,Condition,Synchronized,notify,notifyAll,wait,thread.sleep,thread.interrupt,interrupted,isInterrupted(),volatile 的线程可见性（只能保证原子操作的可见性）
        //jvm,ParNew,CMS,(参数调优，分代收集的机制，CMS回收的原理，跨代引用的处理方案，标记清除)G1(G1的特性，优势，相比于CMS的优化，回收的原理及实现方案，参数的调优，都涉及到哪些个jvm参数),ZGC(ZGC的优势特性，相比于CMS&G1的区别，回收的原理及实现方案，参数的调优都有哪些，有哪些JVM参数)
        //由JVM内存区域划分牵引出JVM 内存空间模型，线程空间和主内存空间同步问题，引起的线程变量数据不一致性问题，使用volatile来保证数据的可见性，但是由于少了线程的空间copy，类似于CPU的高速缓存而是直接从主内存中做变更，所以相对操作效率较低，但基本受影响较小
        //Perm > MetaSpace的调整，原有的类编译等元数据信息在MetaSpace中，常量池等&静态引用数据则直接放入heap堆中；MetaSpace的参数，配置超出具体参数值后开始进行GC,最大不能超过MaxMetaSpace
        //ThreadLocal原理（与Thread内部变量直接绑定，等）ThreadPool 的线程池的注意事项，FutureTask，Callable 等实现
        //

//        FutureTask
        /*CountDownLatch countDownLatch = new CountDownLatch(2);
        countDownLatch.await();
        */
        LockSupport.park(ReentrantLockTest.class);
        System.out.println("***********");
    }
    public static void lockTest(){
        CountDownLatch countDownLatch = new CountDownLatch(5);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("A");
    }

    public void test() {
        Integer num = 1;
        num++;
        System.out.println(num);
        class a{
            public int num =2;
        }
        a d = new a();
        new Thread(new Runnable() {
            @Override
            public void run() {
                d.num=3;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                d.num=3;
            }
        });

    }


    public static void lock() {
        //
        /*ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        AtomicInteger atomicInteger = new AtomicInteger();


        CountDownLatch countDownLatch  = new CountDownLatch();
        countDownLatch.countDown();
        countDownLatch.await();*/
//        Future future = new FutureTask();
/*

        Unsafe U = Unsafe.getUnsafe();
        System.out.println(U);
*/
/*ReentrantLock reentrantLock = new ReentrantLock();
Condition condition =  reentrantLock.newCondition();
condition.await();
condition.signal();*/
//

        /**
         * Unsafe用于提供操作系统层面的原子操作，内存分配与内存释放，原子对比操作的CAS，线程的挂起与释放等操作
         * 关于Unsafe的介绍:
         * https://www.cnblogs.com/thomas12112406/p/6510787.html
         * https://www.cnblogs.com/linghu-java/p/9686651.html
         * 关于Unsafe.park()挂起线程与object.wait()线程挂起的区别（Java线程等待中Thread.sleep()、Object.wait()、LockSupport.park、UNSAFE.park()的原理与区别）
         * https://blog.csdn.net/weixin_41590779/article/details/107821849
         */



        ReentrantLock reentrantLock = new ReentrantLock();
//        reentrantLock.newCondition()
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程1开始获取锁");
                reentrantLock.lock();
                System.out.println("线程1获取锁成功");
            }
        });
        thread.setName("线程1");
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程2开始获取锁");
                reentrantLock.lock();
                System.out.println("线程2获取锁成功");
            }
        });
        thread1.setName("线程2");

        thread.start();
        thread1.start();

    }

    public static void nodeTest() {

         /*Node node = new Node("node1");
        Node t = new Node("node2");
        node.prev = t;
        t.next = node;

        System.out.println(t);*/
    }

    static class Node {
        private String name;
        public Node prev;
        public Node next;

        public Node(String name) {
            this.name = name;
        }
    }
}
