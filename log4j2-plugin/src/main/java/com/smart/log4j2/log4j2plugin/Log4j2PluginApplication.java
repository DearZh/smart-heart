package com.smart.log4j2.log4j2plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Log4j2PluginApplication {

    static Logger logger = LoggerFactory.getLogger(Log4j2PluginApplication.class);

    public static void main(String[] args) {
        logger.info("****test*****");
        SpringApplication.run(Log4j2PluginApplication.class, args);
    }

}
