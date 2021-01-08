package com.gangtise.cloud.launcher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // api基础信息
                .apiInfo(apiInfo())
                // 控制开启或关闭swagger
                .enable(true)
                // 选择那些路径和api会生成document
                .select()
                // 扫描展示api的路径包
                .apis(RequestHandlerSelectors.basePackage("com.gangtise.cloud.launcher.controller"))
                // 对所有路径进行监控
                .paths(PathSelectors.any())
                // 构建
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // api名称
                .title("公有云API集成服务")
                // api 描述
                .description("集成了华为,阿里,腾讯三家共有云产商API接口的服务，Swagger接口验证当前无需授权，直接调用即可")
                // api 版本
                .version("1.0")
                // 构建
                .build();
    }
}
