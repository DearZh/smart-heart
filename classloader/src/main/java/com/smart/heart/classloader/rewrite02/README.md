# 自定义类加载器第二种用法

该包下的类实现依赖于maven-test-jar 服务 和 maven-test-interface服务， 其中interface服务提供Phone接口，该接口分别由maven-test-jar提供具体实现（IPhone），
该接口包也会被该classloader服务所引入，主要验证被加载类，在主服务中如何被调用的情况。

# 验证结果为：

## 1、

1、 通过PluginClassLoader02可知，使用公用的Interface接口，可以实现直接调用被加载类的方法。 但该接口仍然是被自定义类加载器所加载的，而并非AppClassLoader。
也就是尽管Phone接口在主服务中也是存在的，并且该接口是被AppClassLoader加载到类加载器的，但是当我们使用自定义ClassLoader来获取到对应的IPhone实现类时，此时将该 IPhone类进行实例化，并转换为对应的(
Phone)接口，进行方法调用时，此时的Phone接口所对应的类加载器仍然是自定义类加载器，而并非是APPClassLoader。

原因是:

1、根据类加载的传导规则，该类被该加载器加载后，该类所引用的其他类也都会被该类加载器加载。

2、我们将类强转为接口时，JVM会自动帮我们识别该类所对应的类加载器，然后强转的接口，也会优先从该类所对应的类加载器中去进行获取， 所以尽管Phone接口在APPClassLoader和
自定义类加载器中都存在，但实际上在我们转换为对应的接口或对应的实现类时，则该接口/类必然也是来自于自定义类加载器。

## 2、

根据我们PluginClassLoader02类中的第三个代码片段可知，我们直接new ArrayList()并输出该对象的CLassLoader时为“null”，因为该类是被BootStrapClassLoader来加载的，这没有问题。

但是，当我们自定义一个 ArnoldList extends ArrayList，并去new ArnoldList(),然后输出该对象的ClassLoader时，输出结果为
AppClassLoader，这没有任何问题，因为我们的ArnoldList本身就是被APpClassLoader所加载的。

但是当我们使用：ArrayList list = new ArnoldList();
将ArnoldList对象转换为ArrayList父类或List接口时，此时输出该父类和接口的ClassLoader时，得到的结果也是APPClassLoader。

ArrayList本身是由BootStrapClassLoader所加载的，我们将AppClassLoader所加载的类，实例化并转换为ArrayList后，输出该ArrayList的ClassLoader仍然是APPClassLoader，这是为何呢？

Why?

ArrayList本身是由BootStrapClassLoader所加载的，按照我们上述所说的规则，ArnoldList转换为ArrayList时，由于ArrayList该类首先在ArnoldList所对应的类加载器中查找，
如果查不到则，由父类进行查找，最终该ArrayList类，所对应的类加载器应该是BootStrapClassLoader才对。为何会出现和上述规则不一样的地方？

原因是：

1、实际上将对应的具体类，转换为对应的父类或接口时，根本没有先查找该父类或接口是否在该类加载中存在，存在则使用该类加载器获取，不存在则使用父类进行获取这个说法。

实际流程是，该父类或接口的定义，本身只是对具体子类的引用，实际上在我们通过该所定义的接口，来进行.getClass()的操作时，获取的本身就是该具体的子类。

而并不是该接口的Class()。比如我们针对ArrayList list = new ArnoldList(); 这行代码执行 list.getClass().getClassLoader()时，由于getClass()
本身就是获取的该list的具体引用类的Class ，也就是我们这里所定义的ArnoldList，所以我们输出该getClass的ClassLoader时，本身就应该是AppClassLoader， 所以这其实本身是没有任何问题的。

**总结一下也就是：当我们将自定义类加载(或叫做低级别类加载器)所加载到的类，转换为对应的父类或接口时，该父类或接口所对应的本身就只是该具体子类的引用而已， 所以具体的实现类(子类)
本身是被什么类加载器所加载，转换后对应的父类或接口后，该父类或接口所输出的类加载器本身就还是该实现类(子类)的类加载器。和双亲委派等机制没有任何关系。**

也就是说我们上述案例一中推论出的结果(原因)本身实际是错误的，站不住脚的原因。

那如何证明，我们这里的总结就是站得住脚的呢？凭什么你说这里的getClass()获取的是具体引用类的Class，就是具体引用类的Class呢？而不是我接口的getClass()呢？

OK，接下来看一下getClass的源码，你会发现他是native方法，深追JVM的代码又较为麻烦，所以我们这里用一个最简单的方式来验证一下，也就是我们PluginClassLoader02的最后一行代码，
直接输出该父类所定义的list，输出的结果为："ArnoldList{}"。也就是输出的是该list的引用，最终输出时，输出的还是该具体子类的toString()方法。所以验证完毕。

父类或接口持有的仅是具体子类的引用而已，当我们在getClass()时，实际上获取的是该具体引用对象(子类)的getClass()。所以上述的一切逻辑也都跑的通了。

# TCL(Thread Context ClassLoader)

先介绍一下TCL是什么？在周志明《深入理解JAVA虚拟机》285页有给出针对TCL的场景说明，网上也有很多相关的内容，感觉也都是引用的该书中的一些内容。

```
按照双亲委派模型，越基础的类由越上层的类加载器加载，基础的类型之所以被叫做“基础”，主要是因为它们总是作为用户代码继承、调用API的方式而存在。 但程序的设计往往没有绝对不变的完美规则，如果有基础类型的类要调用用户的代码时，此时该怎么办？
```

白话文解释下就是： 当我们的高层次的ClassLoader加载了对应的接口，而对应的实现类是在低层次的ClassLoader中加载的，那么该接口又该如何调用该具体的实现类呢？
因为低层次的ClassLoader是可以通过双亲委派的机制获取到高层次ClassLoader所加载的类的，但是高层次的接口，是无法访问低层次的实现类的，此时该如何处理呢？

**其实这个问题的本质，和我们上述1、2中所验证的问题本质是一样的，我们直接通过低层次的ClassLoader来实例化该实现类并赋值(转换、强转)为对应的接口也是可以的，
只是在JDK中对这个操作又进行了些许封装，使其更加方便了一些而已。然后就因此而引出了两种概念，一种是TCL，另外一种就是ServiceLoader，封装后的 ServiceLoader刚好可以用来进行解耦合使用。**

OK，以上所举的例子，最常见的就是JDK中的JNDI、JDBC等接口实现，JDBC所存在的问题就是对应的JDBC Connection接口是由JDK所定义的，也就是这部分接口是被BootStrapClassLoader而加载的，
但是对应的JDBC的实现，如Oracle，MySql等，这部分JDBC的具体实现，又是存在于各个对应的Jar包中的，由各个数据库的厂商来进行的具体实现。

此时也就面临了我们上述所提到的问题，那么我们接下来看下对应的JDBC的具体实现，其实就可以一目了然，为什么我会说实际上和上述1、2问题本质一样，JDK只是做了封装而已这句话的道理了。

## DriverManage

先给出结果：

在我们PluginClassLoader02的第二个代码块当中，我们直接使用DriverManage去getConnection()并返回了对应的Connection接口实现。此时输出该Connection
所对应的ClassLoader对应的结果是AppClassLoader，而并不是BootStrapClassLoader(也就是null)。

**所以我们也就可以先知道，所谓的JDBC的实现，实际上还是通过低层次的ClassLoader来加载的对应的实现并返回的对应实现类而已，而并不是真正的让高层次的ClassLoader所加载的类，
可以访问到低层次的ClassLoader中所对应的具体实现类。 实际上的实现也并没有想象中那么复杂，我最初一度很困惑就是困惑在这里，都说TCL的出现破坏了对应的双亲委派的机制，然后可以完美的解决上述提到的
高层次ClassLoader所加载的接口可以访问低层次ClassLoader所加载的实现类的问题，让我一度很困惑这是什么一个什么样的设计才可以达到的效果。
而在我进行了验证并查看了对应的JDBC源码后才知道，实际上并没有那么复杂，只是做了一些包装而已。**

首先：

1、TCL的出现的确破坏了原有的双亲委派的机制。（强调下，这里的破坏没有贬义，在具备明确的目的和充分的理由的情况下，破坏实际也是一种创新）
2、但TCL并没有实现上述所提到可以让高层次的ClassLoader加载的接口可以调用低层次的ClassLoader中所加载的类这种功能，实际上还是低层次的ClassLoader来加载的对应的实现并返回的对应实现类，并进行使用而已了。（二次重复）

接着最初上述关于TCL的介绍，重新进行介绍内容的添加。

那么什么是TCL？

TCL是(Thread Context ClassLoader) 又叫做线程上下文类加载器,线程上下文类加载器可以通过使用Thread类下的setContext()方法进行设置，
如果创建线程时还未设置，则该线程便会从父线程中继承一个，如果在应用的全局范围内都没有设置过的话，那么该Thread的默认上下文类加载器就是AppClassLoader。
（这里其实可以直接看下Launcher的代码，便可直接知晓，在对应的Launcher初始化时，加载完对应的APPClassLoader后，便会将该APPClassLoader设置到主线程的setContext()里面，所以默认
主线程的上下文类加载器就是APPClassLoader，而所有的线程都是由主线程创建的，所以默认也都是直接继承主线程ClassLoader，则也就是都是APPClassLoader了）

有了TCL后呢，程序上就可以做一些“舞弊”的事情，JDBC服务则可以通过这个线程上下文的类加载去加载所需执行的SPI代码，这是父类加载器去请求自类加载器完成类加载的行为，
这种行为实际上是打通了双亲委派模型的层次结构来逆向的使用类加载器，所以已经是违背了双亲委派模型的一般性原则。

什么意思呢？

就是说，在没有TCL之前呢，都是子加载器来委托父加载器来加载类，而有了TCL之后呢。如：BootStrapClassLoader中所加载的类，也可以主动的使用TCL中的线程类上下文类加载器，来让它来主动的加载类了。
所以这个事情，逻辑上来说就破坏了双亲委派模型，这么说其实是没毛病的。以前都是子加载器来委托父加载器加载，现在父加载器呢也可以主动的调用TCL来让它加载类了。所以的确是破坏了原则 ，但是没什么毛病。 破坏的挺好。

所以呢，TCL的出现呢，本质上就已经破坏了原则了，至于你想怎么用，那是另外一回事情了。

比如我们的JDBC的实现，DriverManager本身是JDK的类由BootStrapClassLoader来进行的加载，但是在DriverManager代码内部(初始化时)
,调用了ServiceLoader类来获取对应的Driver.class的具体实现类，在ServiceLoader获取Driver.class的具体实现类时，会先通过 Thread.currentThread()
.getContextClassLoader()来获取当前的线程上下文类加载器，然后使用该类加载器来加载对应的Driver.class的驱动具体实现类。

可以理解吗？这个本身就叫做破坏了。简单点来说，这也没干啥啊，就只是ServiceLoader中使用Thread.currentThread()
.getContextClassLoader()获取了对应的类加载器，然后加载了该类加载器下的驱动就叫做破坏了？是的，为什么这么说呢？
因为DriverManager和ServiceLoader本身都是JDK的方法，是被BootStrapClassLoader来加载的，而你现在的代码内部，主动调用了
低层级的ClassLoader，让该ClassLoader主动的去加载了某些类。所以这的确也就是破坏了原则嘛，从这个角度来看，没毛病吧。

而对于JDBC的实现和ServiceLoader实际上是比较简单的，我们直接看PluginClassLoader02类的第2个代码片段，其中有关于JDBC的测试，然后我们直接进入
DriverManager类中，直接看loadInitialDrivers()方法，还有getConnection()以及registerDriver()方法就行，或者直接debug 357行、587、602、618、641这些代码行也可。
直接看对应的debug上下文，更便捷些。这里唯一要注意的一点就是registerDriver()方法，registerDriver()方法的执行阶段实际上就是在对应的Driver.class的具体驱动实现类被加载后，
比如loadInitialDrivers()执行完后，加载了对应的MySql
jar包下的com.mysql.cj.jdbc.Driver类，此时MySql下的Driver类static{}代码块中，会回调DriverManager.registerDriver()
方法，将该Driver实例，注册到DriverManage中，后续执行的getConnection()也就是通过该回调后的Driver，来获取的具体Connection实例。

而对于ServiceLoader，实际上就只是一层包装，在对应的ServiceLoader内部，当通过TCL获取到对应的类加载器后，无非做的就是通过该类加载器来获取对应的类数据即可。
此处ServiceLoader的做法是先通过ClassLoader.getResources()获取资源数据，然后再通过Class.forName()的方式来获取的对应类资源。 直接通过ServiceLoader的loader()
方法,一层层进去看源码即可，最终较为重要的代码是在ServiceLoader下定义的一个LazyIterator子类里面，ServiceLoader将所获取的资源 最终封装了一个对应的迭代器，只有在调用端，最终next()
执行时，也才会真正触发类的加载，避免了扫描到多个驱动类时，多余加载类等问题。

关于ServiceLoader的概念和使用也比较简单，后面列出一个对应的文章链接，感兴趣的直接看链接即可。

## 提醒的点

以上基本上该介绍的内容，都介绍的差不多了，唯一还需要再提醒的一个点是关于TCL的使用，除了上述中介绍的场景中的使用外，另外一种情形是 当我们的服务中需要自定义类加载器来加载plugin插件包时，大部分情况下都需要使用到该TCL。

主要的场景是因为我们无法把控plugin包中所包含的具体代码中，是否有使用到自定义类加载器相关的功能，比如一般情况下，我们的主服务会定义一些对应的接口jar包，给到各个plugin去实现相应的功能， 然后在主服务中会定义自己的类加载器(
如：ArnoldClassLoader 简称 ACL），来加载这部分Plugin包到我们的服务当中， 那么当业务方在实现接口并开发对应的接口下功能时，
如果需要使用到类加载器来获取一些其它资源时，使用哪个ClassLoader呢？比如在该plugin的代码开发中需要获取到某个app.properties的文件，或者其它文件时，由于该plugin最终是被主服务的ACL来进行加载的，
如果在plugin的代码中，使用APPCLassLoader或其它上层Loader则在主服务中加载后，都将会获取不到该资源，那么对于这种场景下最好的方式则是，对应的plugin代码中直接Thread.current()
.getClassLoader()
获取当前的线程上下文Loader进行操作即可。而在主服务中使用ACL加载Plugin时，也先设置一下该Thread.current()
.setClassLoader(ACL) 为自定义ACL即可。这样就可以方便的解决plugin中使用ClassLoader的问题。

当然，另外一种方便的方式则是plugin中直接使用plugin中的类，来获取ClassLoader，毕竟plugin中的类所对应的ClassLoader最终无论是被什么自定义的CL加载，都无所谓。
我只需要使用该类的ClassLoader来获取资源就行。这种方式也可以，但是尽量还是使用Thread上下文的类加载器，这样外围的主服务中则更加可控一些。比如自定义了多个ClassLoader的场景，
ACL1和ACL2，主服务中来进行Thread.setClassLoader()的切换、而Plugin下的代码中只需要动态获取Thread.getClassLoader()，然后进行使用即可。

比如：主服务中加载完对应的plugin1，并进行对应的接口调用时，在执行对应接口的execute()方法之前，只需要在调用前，设置一下Thread.current()
.setClassLoader(ACL),然后再进行execute()的调用，然后执行完重新调整回去TCL即可。那么在对应的要调用plugin2的execute()方法前，可能会根据该
plugin2的实现逻辑的不同，直接先设置Thread.current().setClassLoader(ACL2)，然后再调用execute()，对应的执行逻辑中只需要获取到对应的ACL2的加载器并进行使用即可。

所以这种方式的ClassLoader的传递和使用更加灵活一些嘛。对于如上plugin的场景当然是更推荐TCL多一些。就是因为plugin是可以其它研发参与研发的，所以更加不可控些，
所以灵活度上则也更重要些，如果可以保证对应的业务逻辑及plugin中都没有使用自定义类加载器来执行特殊业务的情况下，那么还设置什么Thread的上下文类加载器，直接调用就行，方便更多。
所以最终还是根据场景来定了，通用场景可以用TCL，其它场景对应的可使用方式也多种多样了。

以上为一些提示，以下为对应的一些可参考链接：

参考链接，仅供参考，有些文章可能会具备一些迷惑性，更多的是提供一些参考和灵感，最终还是要自行动手才行

1、《深入Java虚拟机》

2、Canal & XPocket 源码

3、[ServiceLoader](https://www.jianshu.com/p/7601ba434ff4)

4、[《深入理解JVM》：类加载机制](https://blog.csdn.net/u011080472/article/details/51329315)

5、[TCL](https://blog.csdn.net/yangcheng33/article/details/52631940) 具备迷惑性

6、[ClassLoader](https://blog.csdn.net/javazejian/article/details/73413292) OK

7、[JAVA类隔离](https://blog.xiaohansong.com/classloader-isolation.html#more)

8、[TCL](https://www.jianshu.com/p/05ec26e25627) 写的感觉内容挺多的、不过没看，记录下

9、[ClassLoader](https://blog.csdn.net/briblue/article/details/54973413) 没看，记录下


