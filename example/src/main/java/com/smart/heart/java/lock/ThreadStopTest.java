package com.smart.heart.java.lock;

import java.util.concurrent.locks.LockSupport;

/**
 * @Description: 关于 interrupt() 的线程中断
 * @Author: Arnold.zhao
 * @Date: 2020/10/22
 */
public class ThreadStopTest {

    /**
     * 一个线程不应该由其他线程来强制中断或停止，而是应该由线程自己自行停止。
     * 所以，Thread.stop, Thread.suspend, Thread.resume 都已经被废弃了。
     *
     * Java Thread.interrupt()方法所提供的线程中断，实际就是从线程外界，修改线程内部的一个标志变量，
     * 或者让线程中的一些阻塞方法，抛出InterruptedException。
     * 以此”通知“线程去做一些事情， 至于做什么，做不做，实际完全是由线程内的业务代码自己决定的。不过一般都是释放资源并结束线程。
     */

    /**
     * 基本概念
     */
    public static void basic() {

        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println();
            }
        });

        testThread.interrupt();         //是给线程设置中断标志;  其作用是中断此线程（此线程不一定是当前线程，而是指调用该方法的Thread实例所代表的线程）
        testThread.isInterrupted();     //只检测中断;  作用于此线程，即代码中调用此方法的实例所代表的线程;作用是只测试此线程是否被中断 ，不清除中断状态。
        testThread.interrupted();       //是检测中断并清除中断状态； 作用于当前线程(作用是测试当前线程是否被中断（检查中断标志），返回一个boolean并清除中断状态，第二次再调用时中断状态已经被清除，将返回一个false)
        Thread.interrupted();

        testThread.interrupt(); //设置指定testThread线程的状态为中断标志，
        testThread.isInterrupted();// 检测当前testThread线程是否被外界中断；是则返回true
        testThread.interrupted();//检测当前testThread线程是否被中断，如果被中断则返回true且清楚中断状态，重新变更为未中断状态；
        Thread.interrupted();//静态方法，与testThread.interrupted()一样，（检测当前testThread线程是否被中断，如果被中断则返回true且清楚中断状态，重新变更为未中断状态；） 作用于当前被执行线程，由于testThread内部线程在执行的时候，是无法获取testThread引用的，所以如果想检测当前自己的线程是否被中断且清除中断状态，则可以使用Thread.interrupted()方法；

    }

    /**
     * 验证一般情况下使用interrupt()中断执行线程的例子
     */
    public static void threadStopTest() {

        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //第一种情况：检测线程是否收到中断信令，收到则返回true，并清除当前的线程状态，重新变更为未中断；
              /*  while (!Thread.interrupted()) {
                    System.out.println("线程内代码执行");
                }
                //此时再检测当前该线程是否收到外界中断信令，得到结果为false，因为使用Thread.interrupted()，在收到中断信令后，会清除当前的线程状态，所以此处进行判断时则返回结果为false，线程状态未收到中断信令
                System.out.println(Thread.currentThread().isInterrupted());
                System.out.println(Thread.currentThread().isInterrupted());
*/
                //第二种情况：检测线程是否收到中断信令，收到则返回true，只是检测当前是否收到中断信令，不清除当前的线程状态，
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("线程内代码执行");
                }
                //此时检测当前该线程是否收到外界中断信令，true表示收到，此处获取结果为 true
                System.out.println(Thread.currentThread().isInterrupted()); //true
                System.out.println(Thread.currentThread().isInterrupted()); //true
                //线程被中断后执行该代码块，进行回收工作
                System.out.println("线程收到外部中断通知，已进行线程内部回收，中断完成");
                /*while (true) {

                }*/
            }
        });
        testThread.start();
        try {
            Thread.sleep(5000);
            //等待5秒后 发出中断信号，通知testThread线程进行中断
            testThread.interrupt();
            //判断当前该线程是否中断完成
            boolean flag = true;
            int index = 0;
            Thread.sleep(1000);
            while (flag) {
                //获取指定线程是否收到中断信号，返回true表示线程已经收到中断信号，但线程正在运行，处理中;或者是已经收到了中断信令，但是选择了不中断继续执行；
                // 如果返回false则存在两种情况
                //1、是当前该线程已经执行完毕，完成中断；由于此时线程已经执行完成了，那么此处再获取该线程的信令时则返回为false，
                //2、该线程没有完成中断，但是该线程代码内部使用了Thread.interrupted() 清除了线程的信令状态，此时则也是返回结果为false，
                System.out.println("检测线程的中断信号：" + testThread.isInterrupted());
                //循环检测10秒钟，10秒后则跳出循环
                Thread.sleep(1000);
                index++;
                if (index == 10) {
                    //停止检测
                    flag = false;
                }
            }
            if (!testThread.isInterrupted()) {
                //TODO: testThread线程中断完成，则执行该代码块
                System.out.println("外部检测testThread中断完成");
            } else {
                //TODO: 否则，则执行另外代码块
                System.out.println("外部检测testThread中断失败");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证线程中断，抛出InterruptedException异常的情况；
     */
    public static void threadStopTest2() {

        /**
         * 当外部调用对应线程进行中断的信令时，如果此时该执行线程处于被阻塞状态，如；Thread.sleep()，Object.wait()，BlockingQueue#put、BlockingQueue#take 等
         * 那么此时通过调用当前线程对象的interrupt方法触发这些函数抛出InterruptedException异常。
         * 当一个函数抛出InterruptedException异常时，表示这个方法阻塞的时间太久了，别人不想等它执行结束了。
         * 当你的捕获到一个InterruptedException异常后，亦可以处理它，或者向上抛出。
         *
         * 抛出时要注意？？？：当你捕获到InterruptedException异常后，当前线程的中断状态已经被修改为false(表示线程未被中断)；
         * 此时你若能够处理中断，则不用理会该值；但如果你继续向上抛InterruptedException异常，你需要再次调用interrupt方法，将当前线程的中断状态设为true。
         *
         */

        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //外部调用信令，要中断该线程时，如果此时线程正在休眠或者阻塞中，则将会抛出异常
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    //第一种情况：进入异常捕获，此处捕获到异常，则说明当前该线程被外界要求进行中断，此时我们可以选择中断该线程；那么此时后续的while循环则将不会被执行，线程执行完毕，则结束；

                    /*if (1 > 0) {
                        return;
                    }
                    */

                    //第二种情况：当然，我们也可以选择收到中断信令后，不进行线程中断；比如，当前的线程的确处于正常的阻塞期间，阻塞完成后，我还是要执行while循环的，等到最终while循环执行完毕后，才正常的结束线程的生命周期；
                    //那么此时，捕获到异常后不做任何操作即可；需要注意的是，此时捕获了InterruptedException异常后，此时的线程状态将会自动被修改为false
                    // （false表示线程没有收到过中断信令，或者是线程已经中断完成，或者是线程使用了Thread.interrupted()清除了信令状态）没有收到过中断信令这个基本是不可能的，只要外部有进行调用，则百分百收到信令，除非是在调用中断信令前，获取了一下线程的状态，此时则肯定是false的;如：先执行，testThread.isInterrupted(),再执行testThread.interrupt();则此时第一个执行的isInterrupted()肯定是false，这个场景意义不大；
                    //1、对于外界来说，收到线程的信令状态是false，则表示该线程已经是执行完成了；当然存在线程信令为false是内部线程自己进行了转换，但实际上并没有停止线程执行的情况；
                    //所以一般情况下，按照约定来说，如果内部线程收到中断请求后，此时如果需要继续执行，不理会外部的中断信令，那么此时可以执行：Thread.currentThread().interrupt();重新将内部状态转换为true
                    //这样，外部线程在重新检测当前线程的信令状态时为true时，则知道，内部线程已经收到了中断信令，而不是一直没有收到中断信令。

                    //此处为false，捕获该中断异常后，将会自动修改线程状态为false，
                    System.out.println("异常" + Thread.currentThread().isInterrupted());
                    //此处由于要继续执行该线程，不执行线程中断，所以重新修改中断状态为true；
                    Thread.currentThread().interrupt();
                    //此时获取结果为true；
                    System.out.println("异常" + Thread.currentThread().isInterrupted());
                }
                while (true) {
                    System.out.println("线程内部执行中");
                }

            }
        });
        testThread.start();

        //发出线程中断信令
        testThread.interrupt();
    }

    /**
     * 约定：
     * 内部中断的线程，如果需要继续执行，则必须重新设置信令状态为true；此时外部调用者才会清楚当前线程已经收到中断信令但是还要继续执行；
     * <p>
     * 什么情况下，线程状态会自动变更为false？
     * <p>
     * 1、线程自动执行完毕后，则状态将会自动置位 false；
     * 2、线程内部使用：Thread.interrupted()方法获取线程状态时，将会自动清除线程状态，使当前线程状态重新更改为false；
     * 3、线程内部如果捕获了，InterruptedException异常，那么此时线程状态也会自动修改为false；
     * <p>
     * 所以，
     * 1、如果是使用Thread.interrupted()来获取线程状态的情况，使用完以后，必须保证线程是正常中断的；如果不能保证，建议使用Thread.currentThread().isInterrupted()来获取线程状态;isInterrupted()方法只获取线程状态，不会更改线程状态；
     * 2、对于线程内使用try catch 捕获了InterruptedException异常的情况，则捕获完以后，一定要做相关操作，而不要只捕获异常，但是不处理该中断信令；
     * 当前捕获到异常后，如果需要中断，则直接中断线程即可
     * 当前捕获到异常后，如果不需要中断，需要继续执行线程，则此时需要执行Thread.currentThread().interrupt();重新更改下自己的线程状态为true，表示当前线程需要继续执行；
     * 当前捕获到异常后，如果不需要中断，而是将异常外抛给上层方法进行处理，那么此时也需要执行Thread.currentThread().interrupt();重新更改下自己的线程状态为true，表示当前线程需要继续执行；
     */

    public static void main(String[] args) {
        threadStopTest2();
        /*
        Thread.currentThread().interrupt();
        System.out.println(Thread.currentThread().isInterrupted());true
        System.out.println(Thread.currentThread().isInterrupted());true
        System.out.println(Thread.currentThread().isInterrupted());true
        System.out.println(Thread.interrupted());true
        System.out.println(Thread.currentThread().isInterrupted());false
        System.out.println(Thread.interrupted());false
        */

    }
}
