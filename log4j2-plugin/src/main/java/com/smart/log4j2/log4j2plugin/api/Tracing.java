package com.smart.log4j2.log4j2plugin.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arnold.zhao
 * @version Tracing.java, v 0.1 2021-09-22 18:39 Arnold.zhao Exp $$
 */
public class Tracing {
    //https://www.docs4dev.com/docs/zh/log4j2/2.x/all/manual-flowtracing.html
    //Tracing 在SLF4J中是不支持该API的。是Log4j2独有的功能。
    static final Logger LOGGER = LogManager.getLogger(Tracing.class);

    public static void main(String[] args) {
        LOGGER.entry(1);
        LOGGER.entry(2);
        LOGGER.entry(3,4,5);
        LOGGER.traceEntry("traceEntry");
        LOGGER.traceEntry();
        LOGGER.traceExit();
        LOGGER.traceExit("D");

        LOGGER.throwing(new RuntimeException());
        LOGGER.catching(new RuntimeException());
    }
}
