package com.smart.heart.jdbc;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/11/16
 */
public class SqlExecuteDDL {

    static String driverClassName = "com.mysql.jdbc.Driver";
    static String jdbcUrl = "jdbc:mysql://40.73.82.1:3306/gangtise_db?useUnicode=true";
    static String jdbcUserName = "gangtise@gangtisedb";
    static String jdbcPassWord = "Qwer!234";

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = dataSource();
        Connection connection = dataSource.getConnection();
        connection.prepareStatement("").execute();
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
