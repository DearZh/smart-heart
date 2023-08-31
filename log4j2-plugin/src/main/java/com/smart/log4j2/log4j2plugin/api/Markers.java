package com.smart.log4j2.log4j2plugin.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arnold.zhao
 * @version Markers.java, v 0.1 2021-09-23 10:29 Arnold.zhao Exp $$
 */
public class Markers {
    //https://blog.gmem.cc/log4j2-study-note
    //https://www.docs4dev.com/docs/zh/log4j2/2.x/all/manual-markers.html
    //http://events.jianshu.io/p/1ff824bc997a
    //SLF4J API也支持 Markers

    private Logger logger = LogManager.getLogger(Markers.class.getName());
    private static final Marker SQL_MARKER = MarkerManager.getMarker("SQL");
    private static final Marker UPDATE_MARKER = MarkerManager.getMarker("SQL_UPDATE").setParents(SQL_MARKER);
    private static final Marker QUERY_MARKER = MarkerManager.getMarker("SQL_QUERY").setParents(SQL_MARKER);
    //配合MarkerFilters即可实现仅仅对SQL更新操作记录日志，或者记录所有SQL操作日志，或者记录全部应用日志。

    public static void main(String[] args) {
        Markers markers = new Markers();
        markers.doQuery("tableA");
        Map<String, String> params = new HashMap<String, String>();
        params.put("column1", "column1Value");
        markers.doUpdate("tableA", params);
    }

    public String doQuery(String table) {
        logger.traceEntry();

        logger.debug(QUERY_MARKER, "SELECT * FROM {}", table);

        String result = "...";

        return logger.traceExit(result);
    }

    public String doUpdate(String table, Map<String, String> params) {
        logger.traceEntry();

        if (logger.isDebugEnabled()) {
            logger.debug(UPDATE_MARKER, "UPDATE {} SET {}", table, formatCols(params));
        }

        String result = "...result ";

        return logger.traceExit(result);
    }

    private String formatCols(Map<String, String> cols) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : cols.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        return sb.toString();
    }
}
