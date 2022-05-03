package com.smart.heart.proxy.javassist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;

/**
 * @author Arnold.zhao
 * @version Test.java, v 0.1 2022-04-30 20:53 Arnold.zhao Exp $$
 */
public class TestCreateClass {

    public static void main(String[] args) throws Exception {
        String writeFilePath = writeFilePath("com.smart.heart.proxy.javassist.TestCreateClass", "TestCreateClass");

        createClass(writeFilePath);
    }


    public static void createClass(String writeFilePath) {
        ClassPool cp = ClassPool.getDefault();
        CtClass ct = cp.makeClass("com.smart.heart.Hello");
        try {

            ct.writeFile(writeFilePath);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取指定类所对应的绝对路径
     *
     * @return
     * @throws Exception
     */
    public static String writeFilePath(String resource, String className) throws Exception {
        ClassLoader classLoader = TestCreateClass.class.getClassLoader();

        Class cl = classLoader.loadClass(resource);
        System.out.println(cl);

        /**
         * 必须先转换对应的路径后，才能正确的获得Resource数据。
         * loaderClass时不需要，因为loaderClass，最终执行URLClassLoader的findClasss()方法时，
         * 内部代码会调用 ：resource.replace('.', '/').concat(".class") 主动进行转换后，再进行类获取，所以外部方法无需再提前进行转换。
         */
        String realResource = resource.replace('.', '/').concat(".class");
        URL url = classLoader.getResource(realResource);
        System.out.println(url.getPath());///W:/JAVA/arnoldworkspace/smart-heart/java-agent/target/classes/com/smart/heart/proxy/javassist/Test.class

        //替换对应的Test.class 转换为以下路径：
        // /W:/JAVA/arnoldworkspace/smart-heart/java-agent/target/classes/com/smart/heart/proxy/javassist/
        return url.getPath().replace(className + ".class", "");
    }

}
