# 自定义ClassLoader的第一种用法：

一般情况下如果我们需要加载非classpath路径下的jar包到JVM中的话，则需要手动读取指定路径下的JAR。 该场景一般存在于,如:SpringBoot的一个项目，最终maven打包后，会将对应的依赖Jar包全部汇总到可执行jar里。
而如果此时某个plugin目录下，存放了一些jar包，那么由于该plugin目录下jar包是可动态增加的,所以此时这批plugin 目录下的jar包，当然是无法打到可执行的Jar里的，那么此时就需要在代码中动态加载这部分jar包。

我们以上述场景为例，先实现一个简单的自定义ClassLoader。 另外，java -jar 启动可执行jar包的方式,是无法指定classpath的. 如果不使用可执行Jar，那么我们代码启动的时候可以使用 java
-cp.;/home/plugin Application.class 指定classpath路径的方式来进行启动 这样的话，就直接由AppClassLoader直接读取plugin目录下的jar包了。
<p/>
这里我们以可执行jar为例。
<p/>

参考链接，仅供参考，有些文章可能会具备一些迷惑性，更多的是提供一些参考和灵感，最终还是要自行动手才行

[关于java -cp 和 jar -jar 区别](https://www.cnblogs.com/klb561/p/10513575.html)

[ClassLoader](https://blog.csdn.net/javazejian/article/details/73413292)

[关于Launcher无法被debug的说明](https://blog.csdn.net/li_xunhuan/article/details/104384383)

[关于类加载传导规则](https://blog.xiaohansong.com/classloader-isolation.html#more)