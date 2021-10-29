package com.smart.log4j2.log4j2plugin.api;

import org.apache.logging.log4j.message.StructuredDataMessage;

/**
 * @author Arnold.zhao
 * @version EventLogger.java, v 0.1 2021-09-23 11:07 Arnold.zhao Exp $$
 */
public class EventLogger {
    public static void main(String[] args) {
        StructuredDataMessage structuredDataMessage = new StructuredDataMessage("id", "msg", "type");

        org.apache.logging.log4j.EventLogger.logEvent(structuredDataMessage);
    }
}
