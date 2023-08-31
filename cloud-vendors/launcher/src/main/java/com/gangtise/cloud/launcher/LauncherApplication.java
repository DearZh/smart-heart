package com.gangtise.cloud.launcher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.gangtise.cloud.launcher.mp.mapper")
public class LauncherApplication {

    public static void main(String[] args) {
        SpringApplication.run(LauncherApplication.class, args);
    }

}
