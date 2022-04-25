package com.smart.heart.classloader.rewrite01;

import sun.misc.URLClassPath;
import sun.net.www.ParseUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * @author Arnold.zhao
 * @version PluginClassLoader01.java, v 0.1 2022-04-21 20:06 Arnold.zhao Exp $$
 */
public class PluginClassLoader011 extends URLClassLoader {
    static URLClassPath ucp;

    public PluginClassLoader011(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public PluginClassLoader011(URL[] urls) {
        super(urls);
        ucp = new URLClassPath(urls);
    }

    public PluginClassLoader011(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    /**
     * 重写fileClass方法，实现plugin目录的读取逻辑
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        /*String path = name.replace('.', '/').concat(".class");
        Resource res = ucp.getResource(path, false);

        byte[] classData = new byte[0];
        try {
            classData = res.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            //使用defineClass生成class对象
            return defineClass(name, classData, 0, classData.length);
        }*/
        return super.findClass(name);
    }

    public static void main(String[] args) {
        try {
            File file = new File("W:\\JAVA\\arnoldworkspace\\smart-heart\\maven-test-jar\\target\\classes");
            URL url = ParseUtil.fileToEncodedURL(file);
            PluginClassLoader011 pluginClassLoader01 = new PluginClassLoader011(new URL[]{url});
            try {
                Class cl = pluginClassLoader01.loadClass("com.smart.heart.classloader.rewrite01.Person");
                try {
                    System.out.println(cl.newInstance().toString());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
