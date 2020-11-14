package com.smart.heat.netty.time;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/11/13
 */
public class TimeServer {
    public void bind(int port) throws Exception {
        //创建两个NioEventLoopGroup实例，NELG是个线程组包含了一组NIO线程，专门用于网络事件的处理，实际上它们就是Reactor线程组；
        //创建两个EventLoopGroup的作用是：

        //bossGroup 用于服务端接收客户端的链接；
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //workGroup则用于进行SocketChannel的网络读写；
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //Netty用于启动NIO服务端的辅助启动类，降低开发复杂度；
            ServerBootstrap b = new ServerBootstrap();
            //调用group方法，将两个NIO线程组当做入参传递到ServerBootstrap中
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)  //设置所要创建的channel 为 NioServerSocketChannel，该对象功能类似于JDK NIO类库中的ServerSocketChannel类
                    .option(ChannelOption.SO_BACKLOG, 1024)//配置NioServerSocketChannel的TCP参数
                    .childHandler(new ChildChannelHandler());//绑定I/O 事件的处理类，它的作用类似于Reactor模式中的Handler类，主要用于处理网络I/O事件，如记录日志，对消息进行解编码等；

            //绑定端口，同步等待成功；调用bind方法绑定监听端口，随后，调用它的同步阻塞方法sync()等待绑定操作完成。最后Netty会返回一个ChannelFuture类（类似于JDK 的Future）主要用于一步操作的通知回调；
            ChannelFuture f = b.bind(port).sync();

            //等待服务端监听端口关闭；调用方法进行阻塞，等待服务端链路关闭之后再退出对应的执行线程；
            f.channel().closeFuture().sync();

        } finally {
            //优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new TimeServer().bind(port);
    }
}
