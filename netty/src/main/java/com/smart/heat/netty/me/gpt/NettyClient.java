package com.smart.heat.netty.me.gpt;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author Arnold.zhao
 * @version NettyClient.java, v 0.1 2023-03-21 13:50 Arnold.zhao Exp $$
 */
public class NettyClient {

    private static final int MAX_RETRY = 5;
    private static final int PORT = 9527;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("客户端连接成功!");
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("客户端连接断开!");
                        // 重连操作
                        connect(bootstrap, HOST, PORT, MAX_RETRY);
                    }
                });

        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("连接成功!");
                } else if (retry == 0) {
                    System.err.println("重试次数已用完，放弃连接！");
                } else {
                    // 第几次重连
                    int order = (MAX_RETRY - retry) + 1;
                    // 本次重连的间隔
                    int delay = 1 << order;
                    System.err.println(new java.util.Date() + ": 连接失败，第" + order + "次重连……");
                    bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
                }
            }
        });
    }
}
