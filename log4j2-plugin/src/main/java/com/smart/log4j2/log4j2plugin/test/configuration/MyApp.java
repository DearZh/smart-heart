package com.smart.log4j2.log4j2plugin.test.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arnold.zhao
 * @version MyApp.java, v 0.1 2021-06-28 19:34 Arnold.zhao Exp $$
 */
public class MyApp {

    private static final Logger logger = LogManager.getLogger(MyApp.class);

    public static void main(String[] args) {
        logger.trace("1、 application.");
        Bar bar = new Bar();
        if (!bar.doIt()) {
            logger.error("4、Didn't do it.");
        }
        logger.trace("5、Exiting application.");

    }
}
