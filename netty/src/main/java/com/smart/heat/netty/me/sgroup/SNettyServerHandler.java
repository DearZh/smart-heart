package com.smart.heat.netty.me.sgroup;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Arnold.zhao
 * @version SNettyServerHandler.java, v 0.1 2023-02-16 13:59 Arnold.zhao Exp $$
 */
public class SNettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当有客户端链接时，将该Channel添加到group中
        SChannelHandler.channelGroup.add(ctx.channel());

        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println(">>>>>>>链接激活>>>>>>>>channel获取到的HOST:" + channel.localAddress().getHostString() + ">>>>>>InetAddress获取到的host：" + InetAddress.getLocalHost().getHostName());

        String str = "欢迎Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n";

        ctx.writeAndFlush(str);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收msg消息
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 收到客户端发送的消息：" + msg);
        //通知客户端链消息发送成功
        String str = "服务端收到：" + new Date() + " " + msg + "\r\n";

//        ctx.writeAndFlush(str);
        //收到消息后，群发给客户端
        SChannelHandler.channelGroup.writeAndFlush(str);
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开链接" + ctx.channel().localAddress().toString());
        // 当有客户端退出后，从channelGroup中移除。
        SChannelHandler.channelGroup.remove(ctx.channel());
    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
