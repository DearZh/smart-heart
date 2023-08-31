package com.smart.heat.netty.guide.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/11/14
 */
public class TimeClient {
    public void connect(int port, String host) throws Exception {
        //配置客户端NIO线程池
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    //设置客户端的通道类型
                    .channel(NioSocketChannel.class)//与服务端配置不同，此处需配置为NioSocketChannel
                    .option(ChannelOption.TCP_NODELAY, true)
                    //通道处理器，> 使用TimeClientHandler 初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            //发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();

            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("链接成功");
                    } else {
                        System.out.println("链接失败");
                    }
                }
            });

            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            //释放NIO线程组
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new TimeClient().connect(port, "127.0.0.1");
    }
}
