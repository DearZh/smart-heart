package com.smart.log4j2.log4j2plugin.usage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

/**
 * @author Arnold.zhao
 * @version Parent.java, v 0.1 2021-09-22 17:36 Arnold.zhao Exp $$
 */
public abstract class Parent {
    protected static final Logger parentLogger = LogManager.getLogger();

    private Logger logger = parentLogger;

    protected Logger getLogger() {
        return logger;
    }

    protected void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void log(Marker marker) {
        logger.debug(marker, "Parent log message");
    }

}
