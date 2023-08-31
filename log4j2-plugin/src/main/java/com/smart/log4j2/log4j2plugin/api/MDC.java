package com.smart.log4j2.log4j2plugin.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.UUID;

/**
 * @author Arnold.zhao
 * @version MDC.java, v 0.1 2021-09-23 14:10 Arnold.zhao Exp $$
 */
public class MDC {

    static final Logger logger = LogManager.getLogger(MDC.class);

    public static void main(String[] args) {
        ThreadContext.push("ID",UUID.randomUUID().toString());
        logger.debug("Message 1");
        logger.debug("Message 2");

        logger.debug(ThreadContext.pop());

    }
}
