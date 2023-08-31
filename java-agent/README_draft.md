
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



