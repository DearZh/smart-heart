package com.smart.heart.classloader.rewrite02;

import com.smart.heart.classloader.inface.re02.Phone;
import sun.net.www.ParseUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnold.zhao
 * @version PluginClassLoader02.java, v 0.1 2022-04-25 10:58 Arnold.zhao Exp $$
 */
public class PluginClassLoader02 {
    static final String SOURCE_FILE = "W:\\JAVA\\arnoldworkspace\\smart-heart\\maven-test-jar\\target\\classes";
    static final String CLASS_NAME = "com.smart.heart.classloader.re02.IPhone";

    public static void main(String[] args) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>1、>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        File file = new File(SOURCE_FILE);
        URL url = ParseUtil.fileToEncodedURL(file);
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});

        Class<?> cl = urlClassLoader.loadClass(CLASS_NAME);

        /**
         * java.net.URLClassLoader@14ae5a5
         */
        System.out.println(cl.getClassLoader());

        /**
         * 直接输出Phone classLoader 为 APPClassLoader，该接口是由系统加载器加载
         * sun.misc.Launcher$AppClassLoader@18b4aac2
         *
         */
        System.out.println(Phone.class.getClassLoader());

        /**
         * 将cl 转换为实例化对象后，再次输出Phone的ClassLoader则为 自定义的URLClassLoader
         * 因为该IPhone对象在被自定义URLCl加载后，根据类加载的传导规则，该IPhone所对应的Phone类也会被
         * 自定义类加载器加载，所以此处输出的Phone实际是被自定义类加载器加载的Phone，而不是AppClassLoader加载的该接口
         * 关于类加载传导规则：https://blog.xiaohansong.com/classloader-isolation.html#more
         */
        Phone phone = (Phone) cl.newInstance();

        /**
         * java.net.URLClassLoader@14ae5a5
         */
        System.out.println(phone.getClass().getClassLoader());


        /**
         * IPhone call in..
         */
        phone.call();

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>2、>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        String jdbcUrl = "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true";
        try {
            System.out.println(Connection.class.getClassLoader());//null
            System.out.println(DriverManager.class.getClassLoader());//null
            Connection connection = DriverManager.getConnection(jdbcUrl, "root", "root");
            System.out.println(connection);//com.mysql.cj.jdbc.ConnectionImpl@dfd3711
            System.out.println(connection.getClass().getClassLoader());//sun.misc.Launcher$AppClassLoader@18b4aac2
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>3、>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        ArrayList arrayList = new ArrayList();
        System.out.println(arrayList.getClass().getClassLoader());//null

        System.out.println(new ArnoldList().getClass().getClassLoader());//sun.misc.Launcher$AppClassLoader@18b4aac2

        ArrayList arnoldList = new ArnoldList();
        System.out.println(arnoldList.getClass().getClassLoader());//sun.misc.Launcher$AppClassLoader@18b4aac2

        ArnoldList arnoldList1 = new ArnoldList();
        System.out.println(((ArrayList) arnoldList1).getClass().getClassLoader());//sun.misc.Launcher$AppClassLoader@18b4aac2

        System.out.println(((ArrayList) new ArnoldList()).getClass().getClassLoader());//sun.misc.Launcher$AppClassLoader@18b4aac2

        System.out.println(((List) new ArnoldList()).getClass().getClassLoader());//sun.misc.Launcher$AppClassLoader@18b4aac2

        System.out.println(arnoldList);//ArnoldList{}


    }

    static class ArnoldList extends ArrayList {
        @Override
        public String toString() {
            return "ArnoldList{}";
        }
    }

}
