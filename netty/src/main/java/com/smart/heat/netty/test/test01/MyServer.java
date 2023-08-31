package com.smart.heat.netty.test.test01;

import com.smart.heat.netty.guide.time.TimeServer;
import com.smart.heat.netty.guide.time.TimeServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Arnold.zhao <a href="mailto:Arnold_zhao@126.com"/>
 * @create 2021-03-27
 */
public class MyServer {

    public static void main(String[] args) {
        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                //设置服务端通道实现类型
                .channel(NioServerSocketChannel.class)
                //设置线程队列，得到链接个数
                .option(ChannelOption.SO_BACKLOG, 128)
                .childHandler(new ChildChannelHandler());
        ServerBootstrap b = new ServerBootstrap();

    }


    private static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }
}
