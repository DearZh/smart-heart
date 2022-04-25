package com.smart.heart.classloader.re01;

import java.util.List;

/**
 * @author Arnold.zhao
 * @version Person.java, v 0.1 2022-04-21 20:11 Arnold.zhao Exp $$
 */
public class Person {

    public void call() {
        System.out.println("call");
    }

    @Override
    public String toString() {
        System.out.println("Person classLoader out> " + Person.class.getClassLoader()
                + "> System classLoader out >" + System.class.getClassLoader()
                + "> List classLoader out >" + List.class.getClassLoader()
                + "> PersonRely classLoader out >" + PersonRely.class.getClassLoader());
        /**
         * TODO:CLASSLOADER
         * System 和 List 类将被BootStrapClassLoader加载，所以输出的ClassLoader结果为null
         * 而
         * Person类由于是被自定义的URLClassLoader加载的，**根据类加载传导规则：JVM 会选择当前类的类加载器来加载所有该类的引用的类**
         * 所以此处Person类所引用的PersonRely类，最终所输出的类加载器也是自定义类加载器。
         *
         * https://blog.xiaohansong.com/classloader-isolation.html#more
         */
        return "maven test jar com.smart.heart.classloader.rewrite01 out";
    }

    public static void main(String[] args) throws Exception {
        Class<?> cl = Person.class.getClassLoader().loadClass("com.smart.heart.classloader.re01.Person");
        Person p = (Person) cl.newInstance();
        System.out.println(p);
        p.call();
    }
}
