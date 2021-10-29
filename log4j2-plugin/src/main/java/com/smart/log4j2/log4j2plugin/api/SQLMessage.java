package com.smart.log4j2.log4j2plugin.api;

import com.smart.log4j2.log4j2plugin.test.configuration.MyApp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.message.Message;

import java.util.Locale;
import java.util.Map;

/**
 * @author Arnold.zhao
 * @version SQLMessage.java, v 0.1 2021-09-23 11:11 Arnold.zhao Exp $$
 */
public class SQLMessage implements Message {
    //同样的Log4j2支持API直接传值Message，但SLF4J不支持
    //https://www.docs4dev.com/docs/zh/log4j2/2.x/all/manual-messages.html
    public enum SQLType {
        UPDATE,
        QUERY
    }

    private final SQLType type;
    private final String table;
    private final Map<String, String> cols;

    public SQLMessage(SQLType type, String table) {
        this(type, table, null);
    }

    public SQLMessage(SQLType type, String table, Map<String, String> cols) {
        this.type = type;
        this.table = table;
        this.cols = cols;
    }

    public String getFormattedMessage() {
        switch (type) {
            case UPDATE:
                return createUpdateString();
            case QUERY:
                return createQueryString();
            default:
                return null;
        }
    }

    public String getFormat() {
        return "getFormat";
    }

    public String getMessageFormat() {
        return type + " " + table;
    }

    public Object[] getParameters() {
        return new Object[]{cols};
    }

    public Throwable getThrowable() {
        return null;
    }

    private String createUpdateString() {
        return "create update " + type + " " + table;
    }

    private String createQueryString() {
        return "create query " + type + " " + table;
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

    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(MyApp.class.getName());

        logger.debug(new SQLMessage(SQLMessage.SQLType.QUERY, "user_table_test"));
        logger.debug(new SQLMessage(SQLMessage.SQLType.UPDATE, "user_table_test"));

        logger.error(">>>>>>>>>>test>>>>>>>>>>>");
        
    }
}
