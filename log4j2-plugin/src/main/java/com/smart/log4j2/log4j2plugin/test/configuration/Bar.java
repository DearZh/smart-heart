package com.smart.log4j2.log4j2plugin.test.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arnold.zhao
 * @version Bar.java, v 0.1 2021-06-28 19:34 Arnold.zhao Exp $$
 */
public class Bar {
    static final Logger logger = LogManager.getLogger(Bar.class.getName());

    public boolean doIt() {
        logger.traceEntry("2、traceEntry");
        logger.error("3、Did it again!");
        return logger.traceExit(false);
    }
}
