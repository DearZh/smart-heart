package com.smart.heart.proxy.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.reflect.Modifier;

/**
 * @author Arnold.zhao
 * @version TestCreateMethod.java, v 0.1 2022-05-01 15:31 Arnold.zhao Exp $$
 */
public class TestCreateMethod {

    public static void main(String[] args) throws Exception {
        String writeFilePath = TestCreateClass.writeFilePath("com.smart.heart.proxy.javassist.MyMain", "MyMain");
        System.out.println(writeFilePath);
//        classAddMethod(writeFilePath);


//        updateMethod(writeFilePath);

        classAddMethodMd(writeFilePath);
    }

    public static void classAddMethod(String writeFilePath) throws Exception {
        ClassPool cp = ClassPool.getDefault();
/*

        String insertClassPath = writeFilePath + "MyMain.class";

        cp.insertClassPath(insertClassPath);
*/

        /**
         * 直接get对应MyMain类的方式，最终是会使用App、ext、BootStrap ClassLoader来进行类的加载。
         * 如果该Class类是无法通过上述ClassLoader获取的情况下，则可以使用insertClassPath方法，添加类路径。有点类似于new UrlClassPath()时，传递的URL。
         * 是相同的效果。
         */
        CtClass ct = cp.get("com.smart.heart.proxy.javassist.MyMain");

        /**
         * 定义方法：方法名为 foo
         * 对应的方法参数类型为：int，int
         *
         */
        CtMethod ctMethod = new CtMethod(CtClass.intType, "foo", new CtClass[]{CtClass.intType, CtClass.intType}, ct);

        /**
         * 定义该方法的方法体内容，它接收一段源代码字符串。javassist最终会将该源代码编译为字节码，替换该方法体内容。
         * Javassist定义了以$开头的特殊标识符：
         * 此处：$1 表示该方法的第一个方法参数，$2 则表示第二个方法参数。
         * $args 表示方法参数数组，类型为Object[]
         * $$ 表示所有参数。
         * ....
         * 具体可参考：深入理解JVM字节码 172P的内容。
         */
        ctMethod.setBody("{int a = $1 * $2;return a;}");

        //在该方法前增加代码块
        ctMethod.insertBefore("System.out.println(\"before\");");

        //在该方法后增加代码块
        ctMethod.insertAfter("System.out.println(\"after\");");

        ctMethod.setModifiers(Modifier.PUBLIC);

        /**
         * 将所定义的方法，传递到该类中。
         */
        ct.addMethod(ctMethod);

        /**
         * 输出该class到指定路径
         */
        ct.writeFile(writeFilePath);


        /**
         * 此处使用getMethod方法，重新获取foo这个方法。
         * 此处另外一个小技巧是：getMethod()方法的第二个传参需要是所对应的参数类型。此处foo方法对应的参数是有两个int，
         * 所以对应的表现方式为：(II)I。
         * 但此时对应的另外一个问题则是，对于其他方法的传参类型，我怎么知道它所对应的表示方式是什么呢？
         * 此处很简单的小技巧是：debug ct.getMethods()方法，然后可以查看该ct下所对应的所有CtMethod对象。
         * 就可以一目了然各个方法对应的表示方式了。
         */
        CtMethod ctMethod1 = ct.getMethod("foo", "(II)I");
        System.out.println(ctMethod1);

        System.out.println(ct.getMethods());


    }

    public static void classAddMethodMd(String writeFilePath) throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass ct = cp.get("com.smart.heart.proxy.javassist.MyMain");
        //定义方法：方法名为 foo 对应的方法参数类型为：int，int
        CtMethod ctMethod = new CtMethod(CtClass.intType, "foo", new CtClass[]{CtClass.intType, CtClass.intType}, ct);
        /**
         * 定义该方法的方法体内容，它接收一段源代码字符串。javassist最终会将该源代码编译为字节码，替换该方法体内容。
         * Javassist定义了以$开头的特殊标识符：
         * 此处：$1 表示该方法的第一个方法参数，$2 则表示第二个方法参数。
         * $args 表示方法参数数组，类型为Object[]
         * $$ 表示所有参数。
         * ....
         */
        ctMethod.setBody("{int a = $1 * $2;return a;}");
        ctMethod.setModifiers(Modifier.PUBLIC);
        //将所定义的方法，传递到该类中。
        ct.addMethod(ctMethod);
        //输出该class到指定路径
        ct.writeFile(writeFilePath);
    }

    public static void updateMethod(String writeFilePath) throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass ct = cp.get("com.smart.heart.proxy.javassist.MyMain");
        CtMethod ctMethod = ct.getMethod("test", "()V");
        ctMethod.insertAfter("System.out.println(123);");
        ct.writeFile(writeFilePath);
    }

}
