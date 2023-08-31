import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Assert;
import org.junit.Test;
import pojo.Bar;
import pojo.Foo;
import pojo.JoeKerouac;
import pojo.People;

import java.io.File;
import java.io.IOException;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author: arnold.zhao
 * @email: zhihao.zhao@ingeek.com
 * @date: 2023/8/30
 */
public class ByteBuddyTest {


    /**
     * 创建一个新的类
     *
     * @throws Exception
     */
    @Test
    public void createClassTest() throws Exception {
        String toString = "hello ByteBuddy";

        //定义一个新类并重写对应的 toString() 方法
        DynamicType.Unloaded<Object> unloaded = new ByteBuddy().subclass(Object.class).name("com.org.bytebuddy.HelloByteBuddy").method(named("toString")).intercept(FixedValue.value(toString)).make();

        //到目前为止，我们只定义并创建了一个动态类型，但是我们没有使用它。由 Byte Buddy 创建的类型使用 DynamicType.Unloaded 的实例表示。
        //此时可以使用 DynamicType.Unloaded 的 saveIn(File) 方法，可以将类存储在给定的文件夹中；或者，使用 inject(File) 方法将类注入到现有的 Jar 文件中。
        //以及将该类给加载到 ClassLoader 中，只有加载到对应的类加载器后，才能真正的在 JVM 中找到该类，并使用它。
        saveIn(unloaded);

        Class<? extends Object> clazz = unloaded.load(ByteBuddyTest.class.getClassLoader()).getLoaded();
        Assert.assertEquals(clazz.newInstance().toString(), toString);

        System.out.println(clazz);
        System.out.println(clazz.newInstance().toString());


        //创建一个新的类，并继承自 ByteBuddyTest
        DynamicType.Unloaded<ByteBuddyTest> unloaded1 = new ByteBuddy().subclass(ByteBuddyTest.class).make();

        //使用ByteBuddyTest.class所对应的 classLoader 来加载该类，并newInstance()实例化该类；此处数据结果则是该 class 所继承父类的 toString()内容
        System.out.println(unloaded1.load(ByteBuddyTest.class.getClassLoader()).getLoaded().newInstance());
    }

    /**
     * 保存该 class 类到文件中（可以方便的查看新生成的 class 的代码结构）
     *
     * @param unloaded
     */
    public static void saveIn(DynamicType.Unloaded unloaded) {

        File file = new File("/Users/zhihaozhao/IdeaProjects/smart/smart-heart/bytebuddy/bytebuddy/src/test/A");
        try {
            unloaded.saveIn(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String toString() {
        return "ByteBuddyTest{}";
    }


    /**
     * 重新定义或者重定基底已经存在的类 (暂不可用）
     *
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Test
    public void rebaseClassTest() throws InstantiationException, IllegalAccessException {

        Foo foo = new Foo();

        System.out.println("替换前" + foo.m());
        //热加载替换，尽管Foo类已经完成了加载，但仍然可以替换该类；此处示例为将Bar 替换 Foo；

        Class<? extends Foo> clazz = new ByteBuddy().rebase(Foo.class).method(named("m")).intercept(FixedValue.value("foo rebase")).make().load(Foo.class.getClassLoader()).getLoaded();

        System.out.println(clazz.newInstance().m());

        //Foo m()方法替换前是输出"foo"，替换后调用 m（）方法输出"bar"
        System.out.println("替换后" + foo.m());

    }

    /**
     * 重新加载类
     * https://notes.diguage.com/byte-buddy-tutorial/creating-a-class.html#loading-a-class
     */
    @Test
    public void redefineClassTest() {
        ByteBuddyAgent.install();

        Foo foo = new Foo();

        System.out.println("替换前" + foo.m());
        //热加载替换，尽管Foo类已经完成了加载，但仍然可以替换该类；此处示例为将Bar 替换 Foo；

        DynamicType.Unloaded<Bar> unloaded = new ByteBuddy().redefine(Bar.class).name(Foo.class.getName()).make();

        saveIn(unloaded);

        unloaded.load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

        //Foo m()方法替换前是输出"foo"，替换后调用 m（）方法输出"bar"
        System.out.println("替换后" + foo.m());

        assertThat(foo.m(), is("bar"));
    }



    /**
     * 代理方法到其它实现
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        String hiMsg = "hi JoeKerouac";
        String helloMsg = "hello JoeKerouac";

        //代理 People 的 say()方法到 JoeKerouac 的sayHi()的方法实现上。
        People hi = build("sayHi");
//        People hello = build("sayHello");

        System.out.println(hi.say());
//        System.out.println(hello.say());

        Assert.assertEquals(hi.say(), hiMsg);
//        Assert.assertEquals(hello.say(), helloMsg);
    }

    private People build(String method) throws Exception {
        DynamicType.Unloaded<People> unloaded = new ByteBuddy().subclass(People.class).name("com.joe.ByteBuddyObject").method(named("say")).
                intercept(MethodDelegation.withDefaultConfiguration().filter(ElementMatchers.named(method)).to(new JoeKerouac())).make();

        saveIn(unloaded);

        Class<? extends People> clazz = unloaded.load(ByteBuddyTest.class.getClassLoader()).getLoaded();
        return clazz.newInstance();
    }


}
