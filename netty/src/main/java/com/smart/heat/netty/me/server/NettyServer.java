package com.smart.heat.netty.me.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * D:\MyDownloads\ChromeDownload\netassist 使用netassist进行网络链接调试
 *
 * @author Arnold.zhao
 * @version NettyServer.java, v 0.1 2023-02-16 10:35 Arnold.zhao Exp $$
 */
public class NettyServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "8008"));

    public static void main(String[] args) throws Exception {

        // 配置服务器
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new NettyServerInitializer());
            //启动服务器
            ChannelFuture f = b.bind(PORT).sync();

            // 等待直到服务器socket关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭所有时间循环以终止所有线程
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
