package com.smart.log4j2.log4j2plugin.loop;

/**
 * @author Arnold.zhao
 * @version LoopStaticClass.java, v 0.1 2021-11-26 17:17 Arnold.zhao Exp $$
 */
public class LoopStaticClass {
    static {
        System.out.println("LoopStaticClass static 初始化开始");
        Loop2StaticClass.test();
        System.out.println("LoopStaticClass static 初始化结束");
    }

    public static void test() {
        System.out.println("调用LoopStaticClass的test静态方法");
    }


    public static void main(String[] args) {
        /**
         * 最终输出结果：
         * LoopStaticClass static 初始化开始
         * Loop2StaticClass static 初始化开始
         * 调用LoopStaticClass的test静态方法
         * Loop2StaticClass static 初始化结束
         * 调用Loop2StaticClass的test静态方法
         * LoopStaticClass static 初始化结束
         *
         * 针对上述Static静态代码块初始时的循环依赖问题，A 依赖 B，B 又依赖 A。此时B初始化A时，由于B是被A依赖而初始化的。所以此时B初始化A的静态代码快会被直接跳过，而是直接执行A.test() 静态方法，而不再重复初始化A。
         * 换句话说：真实的逻辑实际上是：A static 代码块中初始化了 B。此时 B static代码块执行时 又调用了 A.test()，此时A 由于已经执行过了static代码块，尽管没执行完，但还是执行过了，所以A.test()。则不再触发 A的static的静态代码块。而是直接执行 A的test()静态代码块方法。
         *
         *  其实这个东西很深，再接着向下看的话，可以扯出来相关的 JVM对应的类初始化的逻辑，以及比如Spring的三级循环依赖是如何解决的。还有类代码
         *
         * 而对于初始化构造函数中，进行循环的初始化时，则必须是代码块完全执行完成才是完全初始化完成。所以最终循环依赖初始化，则会导致最终的StackOverflowError
         *
         *
         */
        LoopStaticClass loopStaticClass = new LoopStaticClass();
    }
}
