package com.smart.heat.netty.me.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * @author Arnold.zhao
 * @version NettyClient.java, v 0.1 2023-02-16 14:07 Arnold.zhao Exp $$
 */
public class NettyClient {
    public static void main(String[] args) {
        new NettyClient().connect("127.0.0.1", 9527);
    }

    private void connect(String inetHost, int inetPort) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ChannelFuture f = null;
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new ClientChannelInitializer());
            f = b.connect(inetHost, inetPort).sync();
            System.out.println("main connection channelFuture = " + f + " >>>Id=");
            outState(f);
            //https://blog.csdn.net/m0_45406092/article/details/104394617
//            f.channel().close();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            outState(f);
            workerGroup.shutdownGracefully();
            outState(f);
        }
    }

    public void outState(ChannelFuture f) {
        /**
         * 在Netty中，Channel是一个双向通信的数据传输通道，可以用来发送和接收数据。Channel接口中定义了isOpen()和isWritable()两个方法，本文将介绍它们的区别。
         *
         * isOpen()方法
         * isOpen()方法用于判断Channel是否处于打开状态。如果Channel已经被关闭，那么isOpen()方法将返回false，否则返回true。
         *
         * 在Netty中，当一个Channel被关闭时，它将不再接收或发送任何数据。因此，在使用Channel之前，我们通常需要先判断它是否处于打开状态，以避免出现不必要的异常。
         */
        System.out.println("isOpen=" + f.channel().isOpen());
        /**
         * isActive()方法用于判断ChannelHandlerContext是否处于活动状态。如果ChannelHandlerContext已经被关闭或者它所属的Channel已经被关闭，那么isActive()方法将返回false，否则返回true。
         */
        System.out.println("isActive=" + f.channel().isActive());
        System.out.println("isRegistered=" + f.channel().isRegistered());
        /**
         * isWritable()方法
         * isWritable()方法用于判断Channel是否可写。如果Channel当前可以写入数据，那么isWritable()方法将返回true，否则返回false。
         *
         * 在Netty中，当一个Channel的发送缓冲区已满时，它将不再接收新的数据，直到发送缓冲区中的数据被发送出去，腾出空间后才能继续写入新的数据。因此，在使用Channel写入数据之前，我们通常需要先判断它是否可写，以避免出现写入失败的情况。
         */
        System.out.println("isWritable=" + f.channel().isWritable());

    }
}
