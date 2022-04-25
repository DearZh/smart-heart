package com.smart.heart.classloader.rewrite01;

import sun.net.www.ParseUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Arnold.zhao
 * @version PluginClassLoader00.java, v 0.1 2022-04-21 20:51 Arnold.zhao Exp $$
 */
public class PluginClassLoader01 {
    static final String SOURCE_FILE = "W:\\JAVA\\arnoldworkspace\\smart-heart\\maven-test-jar\\target\\classes";
    static final String CLASS_NAME = "com.smart.heart.classloader.re01.Person";

    static final String ERROR_SOURCE_FILE = "W:\\JAVA\\arnoldworkspace\\smart-heart\\maven-test-jar\\target\\classes\\com\\smart\\heart\\classloader\\re01";
    static final String ERROR_CLASS_NAME = "Person";

    static final String JAR_FILE = "W:\\JAVA\\arnoldworkspace\\smart-heart\\maven-test-jar\\target\\maven-test-jar-0.0.1.jar";
    static final String JAR_CLASS_NAME = "com.smart.heart.classloader.re01.Person";


    public static void main(String[] args) throws Exception {
        success();//输出：maven test jar com.smart.heart.classloader.rewrite01 out
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>jar>>>>>>>>>>>>>>>>>>>>>>>");
        jarSuccess();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>error>>>>>>>>>>>>>>>>>>>>>>>>>");
        error();//输出：Exception in thread "main" java.lang.ClassNotFoundException: Person (wrong name: com/smart/heart/classloader/re01/Person)
    }

    /**
     * TODO:CLASSLOADER
     * 我们在以下代码中分别new了新的URLClassLoader(),并传参对应的url传递给了URLClassLoader（后续简写为UCL）对象，那么其中url的作用是：
     * 1、当我们后续调用UCL的loadClass(String className)方法时，UCL由于是继承的ClassLoader，所以最终会执行ClassLoader的loadClass()方法，
     * 按照双亲委派的方式会先交由parent的classLoader进行加载，此处我们new UCL()时，由于没有指定对应的parent，所以最终会调用ClassLoader()的getSystemClassLoader()方法，为当前该UCL的parent。
     * Launcher类初始化时，会先实例化ExtClassLoader和AppClassLoader，此处getSystemClassLoader()方法，获取的结果便是Launcher所返回的AppClassLoader类。所以此处new UCL() 所对应的parent则为AppClassLoader。
     * <p>
     * 2、执行ClassLoader的loaderClass()方法时，由于UCL的AppClassLoader只负责加载classpath路径下的类，而AppClassLoader对应的父类ExtClassLoader则只负责java_home/lib/ext 下的类，
     * 而最终的parent BootStrapClassLoader，则只会加载JDK的类，所以此处最终各Parent则都加载不到当前所指定的className类的情况下，则会重新交由UCL来处理，
     * 此时ClassLoader则会调用UCL的findClass()来获取类资源，当执行UCL的findClass()方法时，则会用到我们最初new UCL()时，所传递的url
     * <p>
     * 3、执行UCL的findClass()方法时，则会先从最初的url中，查询该url下是否包含className资源，如果包含,则得到对应的资源byte[]，
     * 再调用 defineClass()方法，将byte生成解析为JVM可识别的Class对象，并return。
     * <p>
     * 上述就是我们在loadClass时所执行的操作，那么此时我们知道了url的作用，和所传递的className的作用后，再来看下后面所分别列出的success()和error()方法的区别：
     * <p>
     * 此处之所以有两个方法，则是因为一个容易出现的错误，看以下两个方法可知，既然我们的url目的是为了给到UCL读取该路径下的资源，那么我直接指定路径像ERROR_SOURCE_FILE这样，
     * 包含到对应的包名下面，对应的className直接写Person?不行吗?为什么加载会出现java.lang.ClassNotFoundException: Person异常?
     * 之所以会异常的原因是，我们使用ERROR_SOURCE_FILE这样的路径，在执行UCL的findClass()方法时，在该路径下获取className为Person的资源时，的确是可以获取得到资源的byte。
     * 但是在后续执行defineClass()方法,将对应的byte资源转换为Class对象时，则会异常，原因是因为在defineClass()内部执行时，将会执行definePackageInternal()方法
     * 该方法将该类所对应的包名进行解析，并最终存储到ClassLoader对象的packages属性中，然后再调用native方法，将该byte转换为Class对象。
     * 如果我们的className并没有定义包名，或者包名与类中所定义的package 不一致的情况下，则后续在执行native方法，转换class时，将会出现异常。
     * <p>
     * 所以我们在定义className时，需保证该className的名称必须包含该类所在的包路径，且该包路径必须是类中所定义的package 包路径。
     * 这样才能加载成功，否则将导致解析并转换class时，该class中所定义的package包路径，在ClassLoader中并没有提前加载的问题，从而引起异常：
     * ClassNotFoundException: Person (wrong name: com/smart/heart/classloader/re01/Person)
     * 异常提示为：错误的名字 com/smart/heart/classloader/re01/Person,也就是在进行类解析时，我们传递的类名是 Person，
     * 但解析Person字节后得到的名字是，com/smart/heart/classloader/re01/Person 于是便有了上述的ClassNotFoundException异常。
     * </p>
     */

    public static void success() throws Exception {
        URL url = parseURL(SOURCE_FILE);
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url})) {
            System.out.println("1success>>" + urlClassLoader);
            Class<?> object1 = urlClassLoader.loadClass(CLASS_NAME);
            out(object1);
            System.out.println("2success>>" + object1.getClassLoader());
            /**
             * TODO:CLASSLOADER
             *  此处输出该object1所对应的classLoader，由于该obj所有的父parent都没有加载到，所以最终由自定义的new URLClassLoader()进行了加载，
             *  那么此时,输出该obj的classLoader，则一定是URLClassLoader实例对象了，但实际上且并不是这样，输出后的结果为:AppClassLoader，这是为何？
             * 通过debug代码可知，我们new URLClassLoader(url)时，所传递的url，按理说该url是独属于我们所实例化的UCL对象才是，但是不知为何
             * 该ucl所对应的parent，也就是AppClassLoader，里面所对应的url资源中，竟然也包含了我们所传递的这个url资源。
             * AppClassLoader本身是只包含classpath所指定的资源才对，那么我们此处自定义的url，是何时也传递到了AppClassLoader当中的？
             * 由于AppClassLoader中也包含了该url资源，且该AppClassLoader本身还是我们自定义new UCL()的parent，所以这也就导致，
             * 我们在执行loadClass()时，正常流程是 BootStrapClassLoader 和 ExtClassLoader 都获取不到该类时，则由AppClassLoader进行执行，
             * AppClassLoader findClass()执行也获取不到类时，则交由我们最初实例化的UCL()进行findClass()操作，在进行findClass()时，由于我们有传递
             * 正确的url路径，所以在该url下，可以获取到对应的类资源，然后就可以执行defineClass()方法，转换Class等一系列操作了，但实际上却不是这样。
             * <p>
             * 因为当执行到AppClassLoader 执行findClass()时，由于该AppClassLoader中也定义了我们实例化UCL()时所传递的url路径，这就导致了
             * 在AppClassLoader这一层进行findClass()时，也就可以获取到该url下的类资源，然后进行一系列解析为Class的操作，那么该Class生成后，
             * 该Class所对应的classLoader还真就是AppClassLoader，而并不是我们自定义的UCL对象。
             * <p>
             * Why?
             * 以为是URLClassLoader实例化时，通过某种方式传递了url给到了APPClassLoader呢，于是翻遍了代码，想找到对应的一些蛛丝马迹，
             * 然后又在Launcher中debug了AppClassLoader的某些方法，此时发现Launcher竟然是无法debug的，找到篇博客发现Launcher本身是由JVM层面的BootstrapClassLoader进行加载的，
             * 而jvm debug jdi的功能，也是由BootStrapClassLoader加载的，而在加载jdi之前是先加载Launcher的，所以在debug功能实现的时候，Launcher本身就已经加载过了，
             * 这也就导致了Launcher本身是无法被debug的。https://blog.csdn.net/li_xunhuan/article/details/104384383
             * OK，既然无法debug Launcher，那就接着check代码呗，然后吭哧吭哧接着check代码，但是没发现有传递url的情况啊，
             * 陷入了困惑，去Google一下相关的资料，发现完全找不到这类我想要的信息，此时还以为自己是发现了类加载器什么隐藏的特性呢，
             *
             * 于是再次吭哧吭哧翻代码，还是没翻到。中午吃完饭接着查，不知道哪里突然来的灵感，发现我每次执行classloader这个项目时，maven-test-jar这个项目，
             * 也都会被动态的编译生成target目录，结果突然查看我classloader这个项目的maven依赖时，不知道什么时候，我特么把maven-test-jar这个项目给引入到了pom里
             * ，由于maven-test-jar这个服务和classloader这个服务都在同一个idea窗口内，idea内启动的机制是优先去找maven-test-jar target下编译后的包，
             * 所以这就导致了每次classloader服务启动时，AppClassLoader都会加载maven-test-jar/target下的资源。这也就导致了上述所看到的冲突的情况，
             * 实际上自定义new UCL() 时对应传递的url参数是根本没有做过共享的，删除该服务pom下队maven-test-jar的依赖，重新启动测试用例，此时输出
             * CLASS_NAME的类加载器是URLClassLoader@14ae5a5,一切恢复正常。ヽ(ー_ー)ノ -_-||
             *
             *
             *
             * </p>
             *
             * 1、要探究一下什么时候，AppClassLoader add了 new UCL()的url资源。
             * 2、实现并验证 读取jar的功能。按道理都一样，UCL中的URLClassPath，应该本身就支持jar的读取。
             * 3、完全自定义类加载器，剥离，双亲委派的机制。（适用于es5,es6,这种场景继承在一起的情况，canal）
             * 4、看下gravity的分享文档，里面也涉及到较多的classLoader的机制，他们现在是如何使用的以及场景。确认一下。
             */
        }
    }

    public static void error() throws Exception {
        URL url = parseURL(ERROR_SOURCE_FILE);
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url})) {
            Class<?> object1 = urlClassLoader.loadClass(ERROR_CLASS_NAME);
            out(object1);
        }
    }

    public static void jarSuccess() throws Exception {
        /**
         * TODO:CLASSLOADER
         * 关于Jar的解析和读取：
         * URLClassLoader在使用URLClassPath(ucp)获取getSource()资源时,实际上具体的执行方式是交由
         * ucp类内分别定义的Loader类和Loader的两个子类(FileLoader和JarLoader)来完成的,
         * ucp内解析对应的url，如果是包含.jar的情况，则使用JarLoader类来进行解析，并获取对应的 getResource()资源。
         * 我们可以通过debug URLClassPath getLoader() 395行，来观察对应的执行堆栈及上下文。
         */
        URL url = parseURL(JAR_FILE);
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url})) {
            //实际调用的仍然是URLClassLoader中URLClassPath来获取的资源数据，和findClass中获取资源的方式相同。
            /*
            URL url1 = urlClassLoader.getResource("META-INF/app.properties");
            System.out.println(url1);
            */
            Class<?> object1 = urlClassLoader.loadClass(JAR_CLASS_NAME);
            out(object1);
        }
    }


    static URL parseURL(String source) throws Exception {
        /**
         * TODO：入口代码是：Launcher 下 AppClassLoader 类初始化时，需要读取java.class.path下定义的jar包路径，然后需要将该路径转换为URL 由AppClassLoader加载该路径下类数据。
         *      然后此处则是抽出了Launcher.java 下 getFileURL() 方法
         */
        File file = new File(source);
        URL url = ParseUtil.fileToEncodedURL(file);
        return url;
    }

    static void out(Class<?> object) throws InstantiationException, IllegalAccessException {
        System.out.println(object.newInstance().toString());
    }
}
