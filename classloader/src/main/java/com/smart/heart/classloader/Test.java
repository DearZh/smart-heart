package com.smart.heart.classloader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/10/9
 */
public class Test {
    public static void main(String[] args) throws Exception {
       /* out();

        ArnoldClassLoader arnoldClassLoader = new ArnoldClassLoader();*/
        //"com.alibaba.otter.canal.adapter.launcher.rest.controller.test.GangtiseRest"
        //"com.alibaba.otter.canal.client.adapter.es6x.ES6xAdapter"

       /* Class cl = arnoldClassLoader.loadClass("com.alibaba.otter.canal.adapter.launcher.rest.controller.test.GangtiseRest");
        System.out.println(cl);*/
        File file = new File("C:\\Arnold\\workSpace\\canal-master\\canal-master\\client-adapter\\launcher\\target\\canal-adapter\\plugin\\client-adapter.es6x-1.1.5-SNAPSHOT-jar-with-dependencies.jar");
        URL url = file.toURI().toURL();
        ArnoldClassLoader arnoldClassLoader = new ArnoldClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
        System.out.println(arnoldClassLoader);
        //"com/alibaba/otter/canal/client/adapter/es6x/ES6xAdapter.class"
        //META-INF/canal/com.alibaba.otter.canal.client.adapter.OuterAdapter
        InputStream inputStream = arnoldClassLoader.getResourceAsStream("com/alibaba/otter/canal/client/adapter/es6x/ES6xAdapter.class");

     /*   byte[] bytes = new byte[1024 * 10];
        inputStream.read(bytes);
        inputStream.close();
        System.out.println(new String(bytes));
        System.out.println("***********************************");


        Class<?> clazz = arnoldClassLoader.loadClass("com.alibaba.otter.canal.client.adapter.es6x.ES6xAdapter");
        System.out.println(clazz);
        OuterAdapter outerAdapter = (OuterAdapter) clazz.newInstance();
        ClassLoader c1 = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(outerAdapter.getClass().getClassLoader());
        outerAdapter.destroy();
        Thread.currentThread().setContextClassLoader(c1);
        System.out.println(outerAdapter);
        System.out.println(outerAdapter.getClass().getClassLoader());
        System.out.println();*/
    }

    static class ArnoldClassLoader extends URLClassLoader {

        public ArnoldClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        public ArnoldClassLoader(URL[] urls) {
            super(urls);
        }

        public ArnoldClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
            super(urls, parent, factory);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            URL url = getResource(name);
            if (url == null) {

            }
            System.out.println("URL：" + url);
            return super.findClass(name);
        }
    }


    public static void out() {
        ClassLoader classLoader = Test.class.getClassLoader();

        System.out.println(Thread.currentThread().getContextClassLoader());
        System.out.println("****************");
        System.out.println(classLoader);
        System.out.println(classLoader.getParent());
        System.out.println(classLoader.getParent().getParent());
        System.out.println(classLoader.getParent().getParent().getParent());
    }
}
