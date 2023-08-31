package com.smart.heart.jdbc;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/11/23
 */
public class ETL {
    static String driverClassName = "com.mysql.jdbc.Driver";
    static String jdbcUrl = "jdbc:mysql://40.73.82.1:3306/gangtise_db?useUnicode=true";
    static String jdbcUserName = "gangtise@gangtisedb";
    static String jdbcPassWord = "Qwer!234";

    public static void main(String[] args) throws SQLException {

        /*String sql = "insert into annc_level(ID,Annc_Cls_Code,Annc_Cls_Name,Entry_Time) values(?,?,?,?)";

        DataSource dataSource = dataSource();
        PreparedStatement preparedStatement= dataSource.getConnection().prepareStatement(sql);
        Object a =426;
        Object b ="2020-04-13 17:19:32";
        preparedStatement.setObject(1,426);
        preparedStatement.setObject(2,426);
        preparedStatement.setObject(3,b);
        preparedStatement.setObject(4,b);
        preparedStatement.execute();
        */
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("a,b,c,d),(");
        System.out.println(stringBuffer.delete(stringBuffer.length()-3,stringBuffer.length()));

/*
        String a = "a,a,b,";
        System.out.println(a.substring(0, a.length() - 1));
        DataSource dataSource = dataSource();
        PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("select * from cls_lian_v1_article limit 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int count = resultSetMetaData.getColumnCount();
        for (int i = 1; i <= count; i++) {
            System.out.println(resultSetMetaData.getColumnName(i));
        }
        System.out.println("-------------");
        while (resultSet.next()) {
            System.out.println(resultSet.getObject(1));
            System.out.println(resultSet.getObject(2));
        }*/

    }

    public void test() throws SQLException {
        DataSource dataSource = dataSource();
        PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("select count(*) from cls_lian_v1_article");
        ResultSet resultSet = preparedStatement.executeQuery();
        Long dataCount = 0L;
        while (resultSet.next()) {
            dataCount = resultSet.getLong(1);
            System.out.println(dataCount);
        }
        int threadCount = Runtime.getRuntime().availableProcessors();
        System.out.println(threadCount);

        long workerCnt = dataCount / 10000 + (dataCount % 10000 == 0 ? 0 : 1);
        System.out.println(workerCnt);
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
