package com.smart.heart.springjava.auto.service;

import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: arnold.zhao
 * @email: zhihao.zhao@ingeek.com
 * @date: 2023/12/5
 */
class UserServiceTest {

    @Test
    void userName() {
        /**
         *  官网：https://github.com/google/auto/tree/main/service
         *  示例：https://www.baeldung.com/google-autoservice
         *
         * 使用@AutoService 可以避免再自己去 META-INF/services 目录下再自己创建对应的路径了，新引入的google auto 的包，会在代码编译的时候，
         * 自动获取@AutoService 注解所定义的类，然后编译时自动生成对应的META-INF/services下目录文件。
         *
         * @AutoService 的这个能力，其实是使用的 Java 编译期注解处理的能力，类似于 Lombok，详情可以去搜下：Java Annotation Processor
         *
         * 或者看下笔记里的这个文档，做了一些记录：https://app.yinxiang.com/fx/2189197a-4fb6-4cbe-94ff-3aa415eed372
         */
        ServiceLoader<UserService> services = ServiceLoader.load(UserService.class);
        services.forEach(userService -> {
            System.out.println(">>>>>");
            System.out.println(userService.userName());
        });
    }
}