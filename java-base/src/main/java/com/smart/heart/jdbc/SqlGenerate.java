package com.smart.heart.jdbc;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/11/17
 */
public class SqlGenerate {

    static String driverClassName = "com.mysql.jdbc.Driver";
    static String jdbcUrl = "jdbc:mysql://40.73.82.1:3306/gangtise_db?useUnicode=true";
    static String jdbcUserName = "gangtise@gangtisedb";
    static String jdbcPassWord = "Qwer!234";

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = dataSource();
        Connection connection = dataSource.getConnection();
        System.out.println(getSyncSql(connection, "cls_lian_v1_article"));
    }

    public static String getSyncSql(Connection connection, String tableName) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("select * from cls_lian_v1_article limit 1 ");
        ResultSet resultSet = preparedStatement.executeQuery();

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select ");
        for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
            String columnName = resultSetMetaData.getColumnName(i + 1);
            stringBuffer.append("a." + columnName + " as " + columnName + ", ");
        }

        String sql = stringBuffer.substring(0, stringBuffer.length() - 2) + " from " + tableName + " as a";

        return sql;
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
}
