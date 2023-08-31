package com.smart.log4j2.log4j2plugin.loop;

/**
 * @author Arnold.zhao
 * @version Loop2StaticClass.java, v 0.1 2021-11-26 17:17 Arnold.zhao Exp $$
 */
public class Loop2StaticClass {
    static {
        System.out.println("Loop2StaticClass static 初始化开始");
        LoopStaticClass.test();
        System.out.println("Loop2StaticClass static 初始化结束");
    }

    public static void test() {
        System.out.println("调用Loop2StaticClass的test静态方法");
    }
}
