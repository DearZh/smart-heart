轻轻松松学会Java Agent，

轻轻松松玩转Java Agent，

Java Agent有什么作用呢？好处是什么？

好比：位计算的好处是什么呢？放在首位。

# 关于字节码操作的使用

各字节码操作工具的比对。

# 概述

# Instrumentation

JDK从1.5版本开始引入了java.lang.instrument包，开发者可以更方便的实现字节码增强。其核心功能由java.lang.instrument.Instrumentation接口提供，
该接口的方法提供了注册类文件转换器，获取所有已加载类等功能。

java.lang.instrument.Instrumentation 接口常用方法如下：

```

void  
    addTransformer(ClassFileTransformer transformer, boolean canRetransform);

void
    retransformClasses(Class<?>... classes) throws UnmodifiableClassException;

Class[]
    getAllLoadedClasses();

boolean
    isRetransformClassesSupported();        
```

1、addTransformer()方法给Instrumentation注册一个类型为ClassFileTransformer的类文件转换器。

2、retransformClasses() 方法用来对JVM已经加载的类重新触发类加载。

3、getAllLoadedClasses() 方法用来获取当前JVM加载的所有类对象

4、isRetransformClassesSupported() 方法返回一个Boolean值，表示当前JVM配置是否支持类重新转换的特性。

上述提到的类文件转换器，ClassFileTransformer 接口只有一个transform方法,接口定义如下:

```
byte[]
    transform(  ClassLoader         loader,
                String              className,
                Class<?>            classBeingRedefined,
                ProtectionDomain    protectionDomain,
                byte[]              classfileBuffer)
        throws IllegalClassFormatException;
```

其中 className 参数表示当前加载类的类名，classFileBuffer 参数是待加载类文件的字节数组。

调用Instrumentation接口addTransformer()方法注册一个ClassFileTransformer(类文件转换器)后， 后续所有JVM加载类都会被该ClassFileTransformer的transform()
方法所拦截，这个方法接收原类文件的字节数组，在这个方法中可以做任意的类文件的改写，最后返回转换过的字节数组，由JVM加载这个修改过的类文件。

如果transform()方法返回null，表示不对该类进行处理，如果返回值不为null，则JVM会用返回的字节数替换原来类的字节数组。

看到这里，想必我们就已经知晓，如果想要在JVM启动后，动态调整对应类的字节码，然后让JVM重新动态加载该类的字节码，那么必须要使用到 Instrumentation 接口所提供的一系列方法。

那么 Instrumentation 接口只是一个接口，我们如何来获取到 Instrumentation 的对象实例？ 然后使用它呢？

这个时候，就涉及到了 Agent jar的概念。

# Agent Jar

Agent Jar 翻译一下就是 （代理 Jar），这个Jar包做的唯一 一件重要的事情呢，就是获取Instrumentation的对象实例。

那么如何定义好这样一个(代理Jar包)，JVM才会传递Instrumentation的对象实例呢？很简单，我们只需要按照JVM的规范，来创建该Jar包即可。

什么样的规范呢？这就是我们这个java-agent源代码所包含的内容了。

首先，我们先定义一个com.smart.heart.agent.AgentMain类,类中主要定义如下一个方法：

```
public static void premain(String agentArgs, Instrumentation inst){

}
```

此时我们已经定义了AgentMain类，也定义了premain()方法了，那么JVM在何时才会执行premain()方法并传递Instrumentation对象实例呢？
很简单，我们只需要在该jar包内的META-INF/MANIFEST.MF文件中，包含Premain-Class等信息即可，这样JVM就可以识别到该Agent的入口类了。

一个典型的生成好的MANIFEST.MF文件如下：

```
Manifest-Version: 1.0
Premain-Class: com.smart.heart.agent.AgentMain
Archiver-Version: Plexus Archiver
Built-By: Admin
Agent-Class: com.smart.heart.agent.AgentMain
Can-Redefine-Classes: true
Can-Retransform-Classes: true
Created-By: Apache Maven 3.6.3
Build-Jdk: 1.8.0_91
```

可以看到，我们当前该Jar包内的MANIFEST.MF文件中，分别定义了 Premain-Class 和 Agent-Class 信息为 com.smart.heart.agent.AgentMain，通过这种定义的方式，
用来告知JVM该Agent Jar所对应的入口类是什么。

那么实际过程中，如何操作呢？我总不能手动修改该Jar包的MANIFEST.MF文件内容吧？

当然不用，我们只需要在该服务的pom.xml中定义如下plugin即可：

```
<plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>2.4</version>
                <configuration>
                    <archive>
                        <manifestEntries>
                            <!-- 进程启动时加载该Agent jar，指定该Agent Jar的启动类 -->
                            <Agent-Class>com.smart.heart.agent.AgentMain</Agent-Class>
                            <Premain-Class>com.smart.heart.agent.AgentMain</Premain-Class>
                            <Can-Redefine-Classes>true</Can-Redefine-Classes>
                            <Can-Retransform-Classes>true</Can-Retransform-Classes>
                        </manifestEntries>
                    </archive>
                </configuration>
            </plugin>
```

此时我们使用maven package 来生成Jar包时，所生成的Jar包就是一个Agent 包了。

## OK，我们继续

此时我们定义了 com.smart.heart.agent.AgentMain类,并且也定义了premain(String agentArgs, Instrumentation inst)方法， 我们可以在premain()
方法中获取到对应的Instrumentation实例了，那么此时我们就要把Instrumentation给使用起来了。

1、首先，定义一个com.smart.heart.agent.SmartClassFileTransformer类，并实现java.lang.instrument.ClassFileTransformer接口，然后实现该接口的transform()
方法。

```
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer){}
```

2、 然后我们再定义一个 com.smart.heart.agent.AgentTest类，该类的结构很简单，主要代码是main方法启动后，调用foo()方法，并输出一行文本内容即可。

```java
public class AgentTest {
    public static void main(String[] args) {
        AgentTest agentTest = new AgentTest();
        agentTest.foo();
    }

    public void foo() {
        Log.out("AgentTest origin code test");
    }

}
```

3、我们在com.smart.heart.agent.SmartClassFileTransformer类的transform()
方法中，获取对应的com.smart.heart.agent.AgentTest资源后,使用javassist修改对应的字节码, 在foo()方法的执行前后，分别增加了对应的执行代码， 然后返回最新修改后的字节码资源。

代码如下所示：

```
String interceptClassName = "com/smart/heart/agent/AgentTest";
        if (className.equals(interceptClassName)) {
            Log.out("SmartClassFileTransformer > AgentTest > ClassLoader = " + SmartClassFileTransformer.class.getClassLoader());
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass ct = cp.get("com.smart.heart.agent.AgentTest");
                CtMethod ctMethod = ct.getMethod("foo", "()V");

                //在该方法执行前添加执行代码
                ctMethod.insertBefore("System.out.println(\"Agent before code\");");
                //在该方法执行后添加执行代码
                ctMethod.insertAfter("System.out.println(\"Agent after code\");");

                return ct.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
```

4、在对应的com.smart.heart.agent.AgentMain类，premain()方法中，将该SmartClassFileTransformer类添加到对应的Instrumentation中。

```
public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new SmartClassFileTransformer(), true);
    }
```

完成以上步骤后，我们分别做了那些事情呢？

1、定义了一个Agent jar

2、把Instrumentation类实例给使用了起来，给Instrumentation类传递了对应的类转换器，并在对应的类转换器中，重新修改了AgentTest类的字节码资源。

那么此时，我们需要做的是什么呢？如何使用该Agent Jar？也就是如何将该Agent Jar包真正的给使用起来？

有两种使用Agent Jar的方式：

1、一种是使用在JVM启动命令上，传递该Agent jar到该JVM中。

2、另外一种是使用 java attach的方式，将该Agent jar传递到对应的目标java 进程中。

# Java -javaagent

首先，我们需要先执行maven package，将该java-agent服务进行下打包,打包后，我们将在 W:\JAVA\arnoldworkspace\smart-heart\java-agent\target 目录下得到一个
java-agent-1.0-SNAPSHOT.jar 这个Jar包也就是我们即将要使用到的Agent jar包。

target下除了会有jar包外，在对应的target/classes目录下，也会有对应的源码编译后的可执行class文件，我们此时先执行一下com.smart.heart.agent.AgentTest 类：

```
C:\Users\Huawei>cd W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\classes

C:\Users\Huawei>W:

W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\classes>java com.smart.heart.agent.AgentTest
AgentTest origin code test
```

如上，可以看到我们执行com.smart.heart.agent.AgentTest类的main()方法，对应的输出结果为 “AgentTest origin code test”。

我们变更一下对应的启动参数，将原本的java执行命令：java com.smart.heart.agent.AgentTest 变更为：

```
java -javaagent:W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\java-agent-1.0-SNAPSHOT.jar=appid:test,chuancan:
ceshi -cp .;W:\JAVA\arnoldworkspace\smart-heart\java-agent\jar\javassist.jar com.smart.heart.agent.AgentTest
```

然后再次打开CMD执行下AgentTest类,执行结果如下所示：

```
W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\classes>java -javaagent:W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\java-agent-1.0-SNAPSHOT.jar=appid:test,chuancan:ceshi  -cp .;W:\JAVA\arnoldworkspace\smart-heart\java-agent\jar\javassist.jar com.smart.heart.agent.AgentTest
Agent before code
AgentTest origin code test
Agent after code
```

可以看到，我们原本执行 com.smart.heart.agent.AgentTest的main方法时，只输出"AgentTest origin code test"，变成了在输出该内容之前和之后，分别还输出了"Agent before
code"和"Agent after code"。 说明我们对AgentTest类的字节码修改是有效的。

## 原理说明

在我们将原本的JVM启动参数上，增加了-javaagent:agent.jar JVM启动参数后。JVM的执行顺序上，将不会直接执行目标类的main方法，而是在执行对应的main方法之前，
先去执行该agent.jar内所定义的入口类的premain(String agentArgs, Instrumentation inst)方法，然后再去执行目标可执行类的main()方法。

在我们 java-agent-1.0-SNAPSHOT.jar 中定义了对应的入口类为：com.smart.heart.agent.AgentMain，所以就会先去执行AgentMain的premain(String agentArgs,
Instrumentation inst)方法，那么此时我们拿到了对应的Instrumentation的类实例后，也就可以执行我们所定义的一系列代码流程了。

此处需要注意的一个点是：java-agent-1.0-SNAPSHOT.jar 只是我们通过JVM参数注入到该JVM进程的一个Agent Jar包，那么该Agent Jar包中所执行的所有代码，实际上使用的都是该JVM进程中的资源，
比如，你在对应的Agent jar中使用到了javassist.jar中的资源，那么此时如果目标进程中，没有依赖该javassist.jar，也就是在对应的类加载器中找不到该javassist.jar中的资源，那么此时Agent jar
在执行对应代码的时候，必然是会异常的，异常原因则是，在执行对应的代码时，无法获取到javassist中的某些类 NotFountClass。

Agent jar只能说是一种特殊的jar包，这种jar包并不是通过maven的方式进行导入的，而是通过 jvm 参数中添加对应路径的方式，进行加载的该jar包，然后该jar包所对应的执行时机上有所不同而已。本质上，还是一个jar包了。

为什么是说在对应的main方法之前，会先执行一下Agent 的premain()方法，而不是说在JVM的启动前会执行该agent的premain()方法，原因就是在main()方法执行之前呢，
我们当前该JVM所要使用的各种资源，是都已经加载成功了的，比如初始化BootStrap、Ext、AppClassLoader等。既然初始化了AppClassLoader，也就表示我们当前JVM启动时所对应的
各种-classpath中所定义的资源，也都是已经传递到APPClassLoader中的。只是没有进行资源的初始化罢了。（毕竟资源类是只有在使用时也才会被进行加载的）

所以，我们在Agent premain()方法中，执行对应的代码时，实际使用的也是APPClassLoader来进行的相关类加载的操作。

了解了上述的内容后，也就明白了，为什么我们在使用如下启动命令时，会使用 -cp的方式来添加javassist.jar到classpath当中了。 因为如果不添加该javassist.jar到ClassPath中，premain()
方法执行时，也就无法获取到对应的javassist的类资源了。

```
java -javaagent:W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\java-agent-1.0-SNAPSHOT.jar=appid:test,chuancan:
ceshi -cp .;W:\JAVA\arnoldworkspace\smart-heart\java-agent\jar\javassist.jar com.smart.heart.agent.AgentTest
```

此时另外一个问题就来了？那难道说，我Agent 中所用到的所有资源，都要已经提前加载到目标服务的APPClassLoader中吗？

当然也不是，JVM给你一个入口，你就可以自行创造一个世界，比如在执行的premain()方法上，自定义对应的类加载器，然后加载自己的类资源就行了。

将自己的类加载器所对应的类资源和目标进程中的类加载器所对应的类资源进行隔离，也是在真实的服务场景下，经常使用到的一种手段。此处为了方便演示，再去做这个事情就有点背道而驰了，
关于ClassLoader的那点事，详细可以参考本人github中关于ClassLoader的内容，[点击我跳转github](https://github.com/DearZh/smart-heart/tree/master/classloader)

另外一个小问题是：premain(String agentArgs, Instrumentation inst)方法上，agentArgs的内容是如何传参的？

实际就是，我们如下命令当中的java-agent-1.0-SNAPSHOT.jar=appid:test,chuancan:ceshi

"="号后面的内容,最终便会传递到premain()方法的agentArgs参数上。

```
java -javaagent:W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\java-agent-1.0-SNAPSHOT.jar=appid:test,chuancan:
ceshi -cp .;W:\JAVA\arnoldworkspace\smart-heart\java-agent\jar\javassist.jar com.smart.heart.agent.AgentTest
```

以上，相信关于Java Agent 在-javaagent启动参数上的玩法，应该是都比较清晰了。

此处再贴一个《深入JVM字节码》上的一个流程图，相信看到这个图后也应该是一目了然了。

![静态Instrumentation处理过程](jpg/StaticInstrumentation.png)

# JAVA  Attach

JAVA Attach的方式本质上只是使用Java Agent的另外一种方式罢了，我们上面使用JVM参数 -javaagent的方式，只能仅限于main()方法执行前， 进行字节码的操作，存在较大的局限性。 在JDK6开始引入了Attach
Agent方案，可以在JVM启动后的任意时刻通过Attach API的方式远程加载Agent的jar包。

Attach的方式和-javaagent的方式最大的区别是什么呢？实际上就是：一个只能在JVM main方法执行前调用premain()方法操作字节码，而Attach的方式呢
可以随时随地的注入Agent，也就可以随时随地的执行Agent中所定义的premain()方法来操作字节码。

一个是在JVM启动前执行字节码，一个是在JVM启动后随时随地可以执行字节码。时序上的差别，也就导致了两者在使用场景上具备了更大的差别。

我们使用这种随时随地可更改JVM中类的字节码的方式，来实现如下的一个小例子：

1、首先我们先开发一个com.smart.heart.attach.AttachTest类，该类每3s中输出一次"100"。

```java
public class AttachTest {
    public static void main(String[] args) {
        while (true) {
            System.out.println(foo());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int foo() {
        return 100;
    }
}
```

将该AttachTest类进程启动后，我们通过Attach的方式将对应的Agent包注入到该AttachTest的启动进程中。

然后实现动态修改该AttachTest类的foo()方法，将其return 100，调整为return 80，并动态的让JVM重新加载该AttachTest类。实现JVM启动过程中，动态的替换该AttachTest的字节码。

2、我们需要重新调整AgentMain类，AgentMain类作为Agent jar的入口类，在使用-javaagent的方式来注入(加载)Agent jar时，会调用该入口类的premain(String agentArgs,
Instrumentation inst)方法， 但是在使用Attach的方式来注入Agent jar时，则是会调用该入口类的agentmain(String agentArgs, Instrumentation inst)方法，
所以我们需要在AgentMain中先新建该agentmain()方法，如下：

```
public static void agentmain(String agentArgs, Instrumentation inst) {
        Log.out("AgentMain > agentmain > agentArgs = " + agentArgs);
        inst.addTransformer(new SmartClassFileTransformer(), true);
        Class[] classes = inst.getAllLoadedClasses();
        for (int i = 0; i < classes.length; i++) {
            if ("com.smart.heart.attach.AttachTest".equals(classes[i].getName())) {
                try {
                    Log.out("AgentMain > agentmain > reload = " + classes[i].getName());
                    /**
                     * 对已加载类触发重加载
                     */
                    inst.retransformClasses(classes[i]);
                } catch (UnmodifiableClassException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        Log.out("AgentMain > agentmain > end");
    }
```

3、修改SmartClassFileTransformer类，该类原本的代码是仅仅针对com.smart.heart.agent.AgentTest类进行了字节码的修改操作，重新调整该类中的方法，
使其对于com.smart.heart.attach.AttachTest进行拦截，并修改该类中的字节码。

```
if (className.equals("com/smart/heart/attach/AttachTest")) {
            try {
                Log.out("1、SmartClassFileTransformer > AttachTest > ClassLoader = " + SmartClassFileTransformer.class.getClassLoader());

                ClassPool cp = ClassPool.getDefault();

                CtClass ct = cp.get("com.smart.heart.attach.AttachTest");

                CtMethod ctMethod = ct.getMethod("foo", "()I");

                ctMethod.setBody("return 80;");
                return ct.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
```

4、由于是跨进程通信，Attach的发起端需要是一个独立的java程序，这个Java程序只需要调用VirtualMachine.attach()方法开始和目标JVM进行跨进程通信即可。
此处定义一个新的类：com.smart.heart.attach.SmartAttachMain，类代码如下：

```
public static void main(String[] var0) {
        VirtualMachine var1 = null;
        try {
            var1 = VirtualMachine.attach(var0[0]);
            System.out.println("SmartAttachMain:args>" + var0[0]);
            var1.loadAgent("W:\\JAVA\\arnoldworkspace\\smart-heart\\java-agent\\target\\java-agent-1.0-SNAPSHOT.jar");
        } catch (AttachNotSupportedException var17) {
            var17.printStackTrace();
        } catch (IOException var18) {
            var18.printStackTrace();
        } catch (AgentLoadException var19) {
            var19.printStackTrace();
        } catch (AgentInitializationException var20) {
            var20.printStackTrace();
        } finally {
            try {
                var1.detach();
            } catch (IOException var16) {
                var16.printStackTrace();
            }

        }

    }
```

该类的主要操作便是，使用VirtualMachine.attach()对应的进程ID, 然后将对应的Agent jar loadAgent()到目标的进程即可。

需要注意的是，VirtualMachine类是com.sun.tools.attach包下的类，所以在编写该类的时候需要先将JDK\lib\tools.jar包下的类，导入到该项目的dependencies下。
在运行该类时，也需要传递对应的JDK\lib\tools.jar到该执行类的classpath下即可。

## 开始执行

1、javac 编译下该 SmartAttachMain.java类，将其生成的class文件，直接编译到该attach目录下即可。

```
W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\classes>cd W:\JAVA\arnoldworkspace\smart-heart\java-agent\src\main\java\com\smart\heart\attach

W:\JAVA\arnoldworkspace\smart-heart\java-agent\src\main\java\com\smart\heart\attach>W:

W:\JAVA\arnoldworkspace\smart-heart\java-agent\src\main\java\com\smart\heart\attach>javac SmartAttachMain.java

W:\JAVA\arnoldworkspace\smart-heart\java-agent\src\main\java\com\smart\heart\attach>
```

2、注释掉 SmartAttachMain.java 中的源码，然后执行java-agent 项目 maven package打包。

为什么不在打包时将SmartAttachMain.java类直接编译到对应的target/classes下？

因为我此处编写SmartAttachMain.java时，并没有将该tools.jar添加到pom.xml中，只是添加到了idea的dependencies下。所以
如果不注释掉SmartAttachMain.java类代码，将会导致package异常。(这里将会引申出后续针对该项目结构的说明，此处知晓即可。)

3、OK，打包完后，开始执行代码进行验证。先运行我们所新建的com.smart.heart.attach.AttachTest类

执行完成后，该进程每3秒输出一次100

```
W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\classes>java -cp .;W:\JAVA\arnoldworkspace\smart-heart\java-agent\jar\javassist.jar com.smart.heart.attach.AttachTest
100
100
100
```

4、新开CMD窗口，使用JPS 查看AttachTest进程号，然后运行com.smart.heart.attach.SmartAttachMain类，此时attach远程28252进程完成，并成功注入Agent jar类

```
W:\JAVA\arnoldworkspace\smart-heart\java-agent\src\main\java>jps
10304 Jps
27092
43512
28252 AttachTest

W:\JAVA\arnoldworkspace\smart-heart\java-agent\src\main\java>java -cp .;W:\JAVA\JDK\jdk-8u91-windows-x64\lib\tools.jar com.smart.heart.attach.SmartAttachMain 28252
SmartAttachMain:args>28252

```

5、切换到原CMD窗口，查看每3s中输出一次的结果，由100，变为输出80。表示新的字节码类修改并重加载到对应JVM中完成。

```
W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\classes>java -cp .;W:\JAVA\arnoldworkspace\smart-heart\java-agent\jar\javassist.jar com.smart.heart.attach.AttachTest
100
100
100
AgentMain > agentmain > agentArgs = null
AgentMain > agentmain > reload = com.smart.heart.attach.AttachTest
1、SmartClassFileTransformer > AttachTest > ClassLoader = sun.misc.Launcher$AppClassLoader@73d16e93
2、SmartClassFileTransformer > AttachTest
3、SmartClassFileTransformer > AttachTest
4、SmartClassFileTransformer > AttachTest
5、SmartClassFileTransformer > AttachTest
AgentMain > agentmain > end
80
80
```

# 项目结构

该项目源代码均托管在github上，源码地址为 [java-agent](https://github.com/DearZh/smart-heart/tree/master/java-agent)

项目结构实际上是非常简单的，按理说无需赘言，但为了避免后来者采坑，或者未来的某个时间点自己在看该文章时自己踩到坑，所以此处提前做下相关的赘述说明。

1、首先java-agent服务下的所有代码，并非都在生成Agent jar后有用到，实际上对于整个Agent jar来说，核心的代码仅仅是： com.smart.heart.agent.AgentMain 以及
com.smart.heart.agent.SmartClassFileTransformer 两个类。

2、而对于com.smart.heart.agent.AgentTest 和 com.smart.heart.attach.AttachTest 两个类

这两个类本身就是独立的具备main()方法的类，所以实际上无论放在那里都可以，之所以放在java-agent服务下，只是为了方便而已。

在我们执行具体的功能验证时，AgentTest 和 AttachTest 也都是以单独进程的方式来进行启动的，而Agent jar无非是通过两种不同的方式注入到对应的进程上而已。

3、 而对于com.smart.heart.attach.SmartAttachMain 该类也是具备独立的main()方法的类，换句话说该类无论放在任何一个地方也都是可以的，之所以放在java-agent服务下，也只是为了方便而已。

SmartAttachMain 类是作为Attach的发起端的Java类而存在的。目的是为了运行该类时，加载对应的Agent jar注入到对应的attach 进程中。

3、对于com.smart.heart.proxy包下的类，是为了验证字节码的操作工具javassist的使用。和最终生成Agent jar后也没有太大关联。

总结一下：java-agent服务源码，只能说是将java-agent中所涉及到的各个技术点，全部整合到该一个服务当中了，便于进行测试和验证而已了。

但实际上在真实的开发场景下，开发者需要知晓，该java-agent服务下所涉及到的相关代码，实际是可以分拆为5个不同的服务的。

比如：AgentTest 和 AttachTest是对应两套真实的业务服务，SmartAttachMain是一套单独的Attach服务，AgentMain和SmartClassFileTransformer对应一套真实的Java
Agent服务，而关于字节码操作javassist的测试用例，则是另外的test服务。

# 总结

关于Agent的使用呢，实际上也是较为简单的，而关于字节码的操作呢，实际上也有很多的现有选择可以使用。ASM、javassist、byte-buddy

对于Agent的使用场景，APM中使用Agent进行打点，以及使用Agent 实现类的热加载的效果、代码诊断工具Arthas等等。

由于各个中间件一般都会存在针对业务方服务进行字节码操作的需求，所以为了避免各中间件团队都开发对应的Agent jar，从而导致各Agent和Agent之间冲突的情况产生。
一般情况下企业内都会开发一个对应的Agent操作平台，其它中间件只需要基于该Agent平台的规则开发对应的plugin插件即可，由Agent jar统一加载对应的plugin到具体的业务服务当中进行相关字节码的注入操作。
这样做的好处，当然就是统一了，统一入口对于企业内来说是件很重要的事情。




-----------------

Agent只是使用Instrumentation的一种方式而已。无论是通过JVM启动时的 java -javaagent的方式注入agent jar， 还是在JVM启动后，使用java attach的方式注入agent
jar，目的都是为了使用该agent jar中所定义的Instrumentation的功能而已。

Agent只是一种壳子，一种使用Instrumentation的壳子。只是说要想使用Instrumentation，必须要有Agent的这种外部的方式（壳子）才行（才能加载才能运行）。

为什么要有这种壳子，因为使用这种壳子的方式定义了对应要加载的类和premain()方法后，在agent被注入后，JVM 会调用premain()方法，传递对应的

Instrumentation的实例，此时才能使用该Instrumentation(类)，来实现对应的一系列功能。

# 前置说明

以下关于Java Agent 和 Java Attach 的使用，均是以当前该服务的绝对路径来进行的验证，所以此处先行贴上前置说明：

当前该服务源码所对应的工作目录为：W:\JAVA\arnoldworkspace\smart-heart\java-agent\

源码编译后所对应的工作路径为：W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\classes

# Java Agent

Instrumentation的第一种使用方式是通过JVM的启动参数 -javaagent的方式来启动，为了能让JVM识别到Agent的入口类，需要在Jar包内的
META-INF/MANIFEST.MF文件中指定Premain-Class等信息。

需要再看下177P的内容即可。177P内容很适合这里。

com.smart.heart.agent.AgentTest类是当前Java Agent模式下主要用于验证的类，该类启动后默认情况下将输出“AgentTest origin code test”

1、在对应的maven pom.xml中定义maven-jar-plugin插件，设置Agent-Class和Premain-Class两个属性值为：com.smart.heart.agent.AgentMain

设置完该plugin后，执行maven package打包时，对应的META-INF/MANIFEST.MF文件内容如下：

主要定义了该jar包所对应的Premain-Class和Agent-Class的类路径为：com.smart.heart.agent.AgentMain

```
Manifest-Version: 1.0
Premain-Class: com.smart.heart.agent.AgentMain
Archiver-Version: Plexus Archiver
Built-By: Admin
Agent-Class: com.smart.heart.agent.AgentMain
Can-Redefine-Classes: true
Can-Retransform-Classes: true
Created-By: Apache Maven 3.6.3
Build-Jdk: 1.8.0_91
```

# Java Attach

在JDK5中，开发者只能在JVM启动时指定一个javaagent，在premain中操作字节码，这种Instrumentation的方式仅限于main方法执行前进行字节码的动态修改，存在较大的局限性。

而在JDK6以后，引入了动态Attach Agent的方案，可以在JVM启动后的任意时刻使用Attach API远程加载Agent的jar包。

通过这种方式，也就实现了 在JVM进程启动的过程中，可以随时随地的注入Agent。而注入Agent也就意味着我们可以随时随地的使用Instrumentation来动态的操作类的字节码，
并且由于是在JVM启动过程中修改的类字节码，通过使用Instrumentation API，使JVM重新加载该字节码类，也就可以达到JVM启动过程中，动态热加载的过程。

由于是在JVM运行过程中，动态更改类的字节码内容，也就可以达到各种神奇的效果。

Java agent的方式或者attach的方式，实际上都是将该Agent注入到一台正在运行或正准备运行main()方法的JVM当中。

由于是将该 agent jar注入到对应的进程中。所以该agent被注入后，所要执行的一系列代码，实际上还是要依赖于该进程才行。

所以该agent jar中所使用到的外部jar包中的功能，也必须已经在该JVM进程的classpath上才行。或者在agent jar 注入后，执行该agent jar中的方法时，需要自定义ClassLoader加载该部分的jar包也才行。

毕竟agent jar，只是通过外部注入的方式，注入到该JVM进程中的一段功能（或者说代码块而已）。

由于是在windows环境下进行的验证，所以先行贴出对应的绝对路径URL，便于后续理解：

验证Java Agent的服务效果：

1、打开cmd cd到对应的class文件夹下：cd W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\classes

2、执行命令： java -javaagent:W:\JAVA\arnoldworkspace\smart-heart\java-agent\target\java-agent-1.0-SNAPSHOT.jar=appid:
test,chuancan:ceshi -cp .;W:\JAVA\arnoldworkspace\smart-heart\java-agent\jar\javassist.jar
com.smart.heart.agent.AgentTest

启动AgentTest类，并在对应的classpath中添加了 javassist.jar jar包，为什么要在主应用程序中添加javassist的jar包，此处需要额外做下说明：

首先，当我们启动

使用Java -javaagent: 的方式来启动，和使用Java -attach的方式启动，两者的使用差别和效果是不同的。



