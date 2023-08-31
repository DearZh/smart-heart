package com.smart.log4j2.log4j2plugin.loop.StackOverflowError;

/**
 * @author Arnold.zhao
 * @version LoopClass.java, v 0.1 2021-11-26 17:10 Arnold.zhao Exp $$
 */
public class LoopClass {
    public LoopClass() {
        System.out.println("LoopClass 初始化函数执行");
        Loop2Class loop2Class = new Loop2Class();
        System.out.println("LoopClass 初始化完成执行");
    }


    public static void main(String[] args) {
/**
 * 循环初始化，则最终：StackOverflowError
 */
        LoopClass loopClass = new LoopClass();
    }
}
