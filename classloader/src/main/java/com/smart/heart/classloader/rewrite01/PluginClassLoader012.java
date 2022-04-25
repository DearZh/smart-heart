package com.smart.heart.classloader.rewrite01;

import java.io.*;

/**
 * @author Arnold.zhao
 * @version PluginClassLoader02.java, v 0.1 2022-04-21 20:46 Arnold.zhao Exp $$
 */
public class PluginClassLoader012 extends ClassLoader {
    private String rootDir;

    public PluginClassLoader012(String rootDir) {
        this.rootDir = rootDir;
    }

    /**
     * 编写findClass方法的逻辑
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 获取类的class文件字节数组
        byte[] classData = getClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            //直接生成class对象
            return defineClass(name, classData, 0, classData.length);
        }
    }

    /**
     * 编写获取class文件并转换为字节码流的逻辑
     *
     * @param className
     * @return
     */
    private byte[] getClassData(String className) {
        // 读取类文件的字节
        String path = classNameToPath(className);
        try {
            InputStream ins = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesNumRead = 0;
            // 读取类文件的字节码
            while ((bytesNumRead = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesNumRead);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 类文件的完全路径
     *
     * @param className
     * @return
     */
    private String classNameToPath(String className) {
        return rootDir + File.separatorChar
                + className.replace('.', File.separatorChar) + ".class";
    }

    public static void main(String[] args) throws ClassNotFoundException {
//        String rootDir = "/Users/zejian/Downloads/Java8_Action/src/main/java/";
        String rootDir = "W:\\JAVA\\arnoldworkspace\\smart-heart\\maven-test-jar\\target\\classes";
        //创建自定义文件类加载器
        PluginClassLoader012 loader = new PluginClassLoader012(rootDir);

        try {
            //加载指定的class文件
//            Class<?> object1 = loader.loadClass("com.zejian.classloader.DemoObj");
//            Class<?> object1 = loader.loadClass("com.smart.heart.classloader.re01.Person");
            Class<?> object1 = loader.findClass("com.smart.heart.classloader.re01.Person");
            System.out.println(object1.newInstance().toString());
            System.out.println(object1.getClassLoader());
            System.out.println(object1.hashCode());
            System.out.println(">>>>>>>>");
            System.out.println(new PluginClassLoader012(rootDir).findClass("com.smart.heart.classloader.re01.Person").getClassLoader());
            //输出结果:I am DemoObj
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
