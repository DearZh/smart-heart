package com.smart.heart.classloader.xpocket2023;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Arnold.zhao
 * @version Test.java, v 0.1 2023-03-06 18:35 Arnold.zhao Exp $$
 */
public class Test {

    static void clJdk8() throws MalformedURLException, InvocationTargetException, IllegalAccessException {
        File file1 = new File("D:\\WorkData\\gerenproject\\GitHub\\Xpocket\\xpocket-main-fork\\xpocket\\xpocket-deploy\\tools\\windows\\tools.jar");
        URLClassLoader classloader = (URLClassLoader) Test.class.getClassLoader();
        Method addUrl = null;
        try {
            addUrl = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        addUrl.setAccessible(true);
        addUrl.invoke(classloader, new Object[]{file1.toURI().toURL()});
    }

    static void clJdk11() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        boolean isJdk8 = false;
        Class builtinClass = null;
        try {
            builtinClass = Class.forName("jdk.internal.loader.BuiltinClassLoader");
        } catch (ClassNotFoundException e) {
            isJdk8 = true;
        }
        if (isJdk8) {
            //执行jdk8代码
        } else {
            /*
            Field ucpField = builtinClass.getDeclaredField("ucp");

            ucpField.setAccessible(true);
            ucpField.get(Thread.currentThread().getContextClassLoader());
*/
            builtinClass = Class.forName("jdk.internal.loader.ClassLoaders$AppClassLoader");
            /*Field ucpField = builtinClass.getDeclaredField("ucp");
            ucpField.setAccessible(true);//异常：at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:340)
            ucpField.get(Thread.currentThread().getContextClassLoader());*/

            Method method = builtinClass.getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
            method.setAccessible(true);
            method.invoke(Thread.currentThread().getContextClassLoader(),"DDDDD");

            System.out.println(">>>>>>>>>>>>>>>");

            B b = new B();
            b.setName("NameA");
            b.setAge("23");
            Class A = Class.forName("com.perfma.xlab.xpocket.launcher.XPocketLauncher$A");
            Field field = A.getDeclaredField("name");
            field.setAccessible(true);
            System.out.println(field.get(b));

            Field addressField = A.getDeclaredField("address");
            addressField.setAccessible(true);
            System.out.println(addressField.get(b));
            //执行jdk11
            System.out.println(">>>>>>");
        }

    }

    static class A {
        private final String address = "Beijing";
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class B extends A {
        private String age;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

}
