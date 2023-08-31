package com.smart.heart.mybatisplus.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/12
 */
@Configuration
public class MybatisConfig {
    //配置分页插件，否则直接使用Page进行分页则无效：https://mp.baomidou.com/guide/page.html

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

    //Mybatis plus & Mybatis 常用配置 https://baomidou.com/config/#checkconfiglocation

    @Bean
    public MybatisConfiguration mybatisConfiguration() {
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        //开启驼峰映射 （Mybatis 默认驼峰映射是false，但 MP中 该参数默认为 true，所以此处配不配置也无所谓了）
        mybatisConfiguration.setMapUnderscoreToCamelCase(true);
        return mybatisConfiguration;
    }

}
