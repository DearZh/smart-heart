package com.smart.log4j2.log4j2plugin.loop.StackOverflowError;

/**
 * @author Arnold.zhao
 * @version Loop2Class.java, v 0.1 2021-11-26 17:10 Arnold.zhao Exp $$
 */
public class Loop2Class {
    public Loop2Class() {
        System.out.println("Loop2Class 初始化函数执行");
        LoopClass loopClass = new LoopClass();
        System.out.println("Loop2Class 初始化完成执行");
    }
}
