package com.smart.heat.netty.official.echo01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.nio.charset.Charset;

/**
 * Echoes back any received data from a client.
 */

/**
 * 回显从客户端收到的所有数据。
 */
public final class EchoServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {
        //配置SSL
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // 配置服务器
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final EchoServerHandler serverHandler = new EchoServerHandler();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            //基于长度进行解码（缓冲区拿到10个字符的长度数据数据后，才会去执行channelRead()方法，如果没有拿到则等待拿到10个后才会执行处理器的channelRead()方法）
//                            p.addLast(new FixedLengthFrameDecoder(10));
                            //基于换行进行解码，只要接受到的文本后面带 \n 则会执行一次channelRead()方法,如果一直没有接收到\n，则表示一行数据一直没有结束，则一直等待\n信号后，
                            // 才会调用对应处理器方法
                            p.addLast(new LineBasedFrameDecoder(1024));
                            p.addLast(new StringDecoder(Charset.forName("GBK")));
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(serverHandler);
                        }
                    });
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