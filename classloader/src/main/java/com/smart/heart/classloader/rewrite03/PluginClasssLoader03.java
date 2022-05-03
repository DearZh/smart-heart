package com.smart.heart.classloader.rewrite03;

import com.smart.heart.classloader.inface.re02.Phone;
import sun.net.www.ParseUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnold.zhao
 * @version PluginClasssLoader03.java, v 0.1 2022-04-28 21:48 Arnold.zhao Exp $$
 */
public class PluginClasssLoader03 {
    static final String CLASS_NAME = "com.smart.heart.classloader.re02.IPhone";


    public static void main(String[] args) throws Exception {
        optimizedTest();
    }

    public static void optimizedTest() throws Exception {
        List<String> sourceFile = new ArrayList<>();
        sourceFile.add("W:\\JAVA\\arnoldworkspace\\smart-heart\\maven-test-jar\\target\\maven-test-jar-0.0.1.jar");
        sourceFile.add("W:\\JAVA\\arnoldworkspace\\smart-heart\\maven-test-jar-2\\target\\maven-test-jar-2-1.0-SNAPSHOT.jar");

        /**
         * 模拟场景，如：
         * es6和es7的包对应的ConnectionAPI是一样的，但ES7是不兼容ES6的，es7的jar包只能连接es7的库，不能连接es6的库，也就是对于这种不兼容的Jar包场景，如何处理？
         * 此时，一个项目中既要使用es6，又要使用es7，如何处理呢？
         * 1、将es6的jar包，重新maven-shard-plugin，将该es6所有类中对应的package的定义，全部调整为新的类路径。这样es7就可以和es6进行融合了。
         * 2、将该两个jar包，分别加载到两个自定义的ClassLoader中。
         * （是两个不同的ClassLoader，分别加载两个jar包。都加载到一个ClassLoader中，根据双亲委派原则，第二个相同类路径的Class就不会再被加载了）
         * 使用第二种方式，也就是如下的代码。
         *
         *  而这种实现方式呢，其实也就是Canal中针对插件加载的实现方式，不过Canal是将es6的jar包，最终把源码也编译到了es6-adapter的jar包中，然后再由canal加载该adapter，调用对应的实现逻辑。
         *
         *
         */
        sourceFile.forEach(filePath -> {
            try {
                URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{ParseUtil.fileToEncodedURL(new File(filePath))});
                Class<?> cl = urlClassLoader.loadClass(CLASS_NAME);
                Phone phone = (Phone) cl.newInstance();

                /**
                 * 实际在当前场景下是否设置线程上下文类加载器无所谓，因为对应的两个jar包中是没有使用到类加载器的，
                 * 但是对于部分plugin包中未知，或者有使用到类加载器的，则必须传递到线程上下文的类加载器中，让该plugin jar包中可以正常的使用自定义的类加载器执行某些操作。
                 * 关于这块的说明：详情看rewrite02中的备注就行。
                 */
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Thread.currentThread().setContextClassLoader(phone.getClass().getClassLoader());
                phone.call();
                Thread.currentThread().setContextClassLoader(classLoader);
                /**
                 * 分别输出：
                 * IPhone call in.. test-jar-1
                 * IPhone call in.. test-jar-2
                 */
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        /**
         * 如下是将上述两个jar包，分别加载到一个自定义类加载器的场景，结果也就是只会加载第一个class了。
         * 根据loadClass的代码执行逻辑：首先会先执行findLoadedClass()（使用当前类加载器）判断该类是否被加载（native方法），
         * 如果没有被加载，则执行双亲委派的一整套逻辑，最终parent无法获取到，则交由当前该类加载器进行findClass()的操作。
         * 那么此处该class由于已经被加载过了，则直接返回该class了，而不会再次进行二次加载，所以最终的输出，也都会只输出相同的结果。
         *
         * 相同类路径下的两个相同类，最终只会被加载一个，最优先被加载类的存在于该类加载器后，后续该加载器就不会重复再进行该类的加载，而是直接return该已被加载的该class。
         *
         * 而这个优先级，就是按照该url的顺序来的，URLClassLoader中使用URLClassPath getResource()获取资源时，首先next各个URL，如果该url可以获取到资源，
         * 则直接从该url出获取resource了。
         */
        URL url1 = ParseUtil.fileToEncodedURL(new File(sourceFile.get(0)));
        URL url2 = ParseUtil.fileToEncodedURL(new File(sourceFile.get(1)));
        URL[] url = new URL[]{url1, url2};

        URLClassLoader urlClassLoader = new URLClassLoader(url);
        Class<?> cl = urlClassLoader.loadClass(CLASS_NAME);
        Phone phone = (Phone) cl.newInstance();

        phone.call();//只会输出：IPhone call in.. test-jar-1

        Class<?> cl1 = urlClassLoader.loadClass(CLASS_NAME);
        Phone phone1 = (Phone) cl1.newInstance();

        phone1.call();//只会输出：IPhone call in.. test-jar-1


    }

    public static void beforeOptimizationTest() throws Exception {
        final String SOURCE_FILE_1 = "W:\\JAVA\\arnoldworkspace\\smart-heart\\maven-test-jar\\target\\classes";

        final String SOURCE_FILE_2 = "W:\\JAVA\\arnoldworkspace\\smart-heart\\maven-test-jar-2\\target\\classes";


        File file = new File(SOURCE_FILE_1);
        URL url = ParseUtil.fileToEncodedURL(file);
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});

        Class<?> cl = urlClassLoader.loadClass(CLASS_NAME);
        Phone phone = (Phone) cl.newInstance();

        phone.call();// IPhone call in.. test-jar-1

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        File file1 = new File(SOURCE_FILE_2);
        URL url1 = ParseUtil.fileToEncodedURL(file1);
        URLClassLoader urlClassLoader1 = new URLClassLoader(new URL[]{url1});

        Class<?> cl1 = urlClassLoader1.loadClass(CLASS_NAME);
        Phone phone1 = (Phone) cl1.newInstance();

        phone1.call();//IPhone call in.. test-jar-2

    }
}
