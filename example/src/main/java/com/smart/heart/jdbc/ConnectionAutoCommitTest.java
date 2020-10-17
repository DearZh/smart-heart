package com.smart.heart.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.sql.DataSource;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.TimeZone;

/**
 * @Description: 测试DataSource不自动提交，手动commit事务提交功能
 * @Author: Arnold.zhao
 * @Date: 2020/10/12
 */
public class ConnectionAutoCommitTest {

    static String driverClassName = "com.mysql.jdbc.Driver";
    static String jdbcUrl = "jdbc:mysql://40.73.82.1:3306/gangtise_db?useUnicode=true";
    static String jdbcUserName = "gangtise@gangtisedb";
    static String jdbcPassWord = "Qwer!234";

    public static void main(String[] args) throws Exception {
        Connection connection = dataSource().getConnection();
        //set autoCommit false ，表示不自动提交SQL，需代码中手动commit
        connection.setAutoCommit(false);
        String insertSql = "insert into cls_lian_v1_article_target(id,title) values(?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setObject(1, 123);
        preparedStatement.setObject(2, "title test");

        preparedStatement.execute();
        preparedStatement.close();

        connection.commit();

//        connection.rollback();
    }

    public static DataSource dataSource() throws SQLException {
        DruidDataSource dataSource;
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUserName);
        dataSource.setPassword(jdbcPassWord);
        dataSource.setInitialSize(1);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(30);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setUseUnfairLock(true);
        dataSource.init();
        return dataSource;
    }

    /**
     * 设置 preparedStatement
     *
     * @param type  sqlType
     * @param pstmt 需要设置的preparedStatement
     * @param value 值
     * @param i     索引号
     */
    public static void setPStmt(int type, PreparedStatement pstmt, Object value, int i) throws SQLException {
        switch (type) {
            case Types.BIT:
            case Types.BOOLEAN:
                if (value instanceof Boolean) {
                    pstmt.setBoolean(i, (Boolean) value);
                } else if (value instanceof String) {
                    boolean v = !value.equals("0");
                    pstmt.setBoolean(i, v);
                } else if (value instanceof Number) {
                    boolean v = ((Number) value).intValue() != 0;
                    pstmt.setBoolean(i, v);
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.CHAR:
            case Types.NCHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                if (value instanceof String) {
                    pstmt.setString(i, (String) value);
                } else if (value == null) {
                    pstmt.setNull(i, type);
                } else {
                    pstmt.setString(i, value.toString());
                }
                break;
            case Types.TINYINT:
                if (value instanceof Number) {
                    pstmt.setByte(i, ((Number) value).byteValue());
                } else if (value instanceof String) {
                    pstmt.setByte(i, Byte.parseByte((String) value));
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.SMALLINT:
                if (value instanceof Number) {
                    pstmt.setShort(i, ((Number) value).shortValue());
                } else if (value instanceof String) {
                    pstmt.setShort(i, Short.parseShort((String) value));
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.INTEGER:
                if (value instanceof Number) {
                    pstmt.setInt(i, ((Number) value).intValue());
                } else if (value instanceof String) {
                    pstmt.setInt(i, Integer.parseInt((String) value));
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.BIGINT:
                if (value instanceof Number) {
                    pstmt.setLong(i, ((Number) value).longValue());
                } else if (value instanceof String) {
                    pstmt.setLong(i, Long.parseLong((String) value));
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.DECIMAL:
            case Types.NUMERIC:
                if (value instanceof BigDecimal) {
                    pstmt.setBigDecimal(i, (BigDecimal) value);
                } else if (value instanceof Byte) {
                    pstmt.setInt(i, ((Byte) value).intValue());
                } else if (value instanceof Short) {
                    pstmt.setInt(i, ((Short) value).intValue());
                } else if (value instanceof Integer) {
                    pstmt.setInt(i, (Integer) value);
                } else if (value instanceof Long) {
                    pstmt.setLong(i, (Long) value);
                } else if (value instanceof Float) {
                    pstmt.setBigDecimal(i, new BigDecimal((float) value));
                } else if (value instanceof Double) {
                    pstmt.setBigDecimal(i, new BigDecimal((double) value));
                } else if (value != null) {
                    pstmt.setBigDecimal(i, new BigDecimal(value.toString()));
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.REAL:
                if (value instanceof Number) {
                    pstmt.setFloat(i, ((Number) value).floatValue());
                } else if (value instanceof String) {
                    pstmt.setFloat(i, Float.parseFloat((String) value));
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.FLOAT:
            case Types.DOUBLE:
                if (value instanceof Number) {
                    pstmt.setDouble(i, ((Number) value).doubleValue());
                } else if (value instanceof String) {
                    pstmt.setDouble(i, Double.parseDouble((String) value));
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
                if (value instanceof Blob) {
                    pstmt.setBlob(i, (Blob) value);
                } else if (value instanceof byte[]) {
                    pstmt.setBytes(i, (byte[]) value);
                } else if (value instanceof String) {
                    pstmt.setBytes(i, ((String) value).getBytes(StandardCharsets.ISO_8859_1));
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.CLOB:
                if (value instanceof Clob) {
                    pstmt.setClob(i, (Clob) value);
                } else if (value instanceof byte[]) {
                    pstmt.setBytes(i, (byte[]) value);
                } else if (value instanceof String) {
                    Reader clobReader = new StringReader((String) value);
                    pstmt.setCharacterStream(i, clobReader);
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.DATE:
                if (value instanceof java.sql.Date) {
                    pstmt.setDate(i, (java.sql.Date) value);
                } else if (value instanceof java.util.Date) {
                    pstmt.setDate(i, new java.sql.Date(((java.util.Date) value).getTime()));
                } else if (value instanceof String) {
                    String v = (String) value;
                    if (!v.startsWith("0000-00-00")) {
                        java.util.Date date = parseDate(v);
                        if (date != null) {
                            pstmt.setDate(i, new Date(date.getTime()));
                        } else {
                            pstmt.setNull(i, type);
                        }
                    } else {
                        pstmt.setObject(i, value);
                    }
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.TIME:
                if (value instanceof java.sql.Time) {
                    pstmt.setTime(i, (java.sql.Time) value);
                } else if (value instanceof java.util.Date) {
                    pstmt.setTime(i, new java.sql.Time(((java.util.Date) value).getTime()));
                } else if (value instanceof String) {
                    String v = (String) value;
                    java.util.Date date = parseDate(v);
                    if (date != null) {
                        pstmt.setTime(i, new Time(date.getTime()));
                    } else {
                        pstmt.setNull(i, type);
                    }
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            case Types.TIMESTAMP:
                if (value instanceof java.sql.Timestamp) {
                    pstmt.setTimestamp(i, (java.sql.Timestamp) value);
                } else if (value instanceof java.util.Date) {
                    pstmt.setTimestamp(i, new java.sql.Timestamp(((java.util.Date) value).getTime()));
                } else if (value instanceof String) {
                    String v = (String) value;
                    if (!v.startsWith("0000-00-00")) {
                        java.util.Date date = parseDate(v);
                        if (date != null) {
                            pstmt.setTimestamp(i, new Timestamp(date.getTime()));
                        } else {
                            pstmt.setNull(i, type);
                        }
                    } else {
                        pstmt.setObject(i, value);
                    }
                } else {
                    pstmt.setNull(i, type);
                }
                break;
            default:
                pstmt.setObject(i, value, type);
        }
    }

    /**
     * 通用日期时间字符解析
     *
     * @param datetimeStr 日期时间字符串
     * @return Date
     */
    public static java.util.Date parseDate(String datetimeStr) {
        if (StringUtils.isEmpty(datetimeStr)) {
            return null;
        }
        datetimeStr = datetimeStr.trim();
        if (datetimeStr.contains("-")) {
            if (datetimeStr.contains(":")) {
                datetimeStr = datetimeStr.replace(" ", "T");
            }
        } else if (datetimeStr.contains(":")) {
            datetimeStr = "T" + datetimeStr;
        }
        TimeZone localTimeZone = TimeZone.getDefault();
        int rawOffset = localTimeZone.getRawOffset();
        String symbol = "+";
        if (rawOffset < 0) {
            symbol = "-";
        }
        rawOffset = Math.abs(rawOffset);
        int offsetHour = rawOffset / 3600000;
        int offsetMinute = rawOffset % 3600000 / 60000;
        String hour = String.format("%1$02d", offsetHour);
        String minute = String.format("%1$02d", offsetMinute);
        String timeZone = symbol + hour + ":" + minute;
        DateTime dateTime = new DateTime(datetimeStr, DateTimeZone.forID(timeZone));

        return dateTime.toDate();
    }
}
