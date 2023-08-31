package com.smart.heat.netty.me.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Arnold.zhao
 * @version NettyServerHandler.java, v 0.1 2023-02-16 10:39 Arnold.zhao Exp $$
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println(">>>>>>>链接激活>>>>>>>>channel获取到的HOST:" + channel.localAddress().getHostString() + ">>>>>>InetAddress获取到的host：" + InetAddress.getLocalHost().getHostName() + ">>>>>channelId:" + channel.id());

        /*SocketChannel channel = (SocketChannel) ctx.channel();
        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        ByteBuf buf = Unpooled.buffer(str.getBytes().length);
        buf.writeBytes(str.getBytes("GBK"));
        ctx.writeAndFlush(buf);
*/

        String str = "欢迎Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n";
        //不用再手动编码了，SocketChannel中已经添加了主动编码的方法
        /*ByteBuf buf = Unpooled.buffer(str.getBytes().length);
        buf.writeBytes(str.getBytes("GBK"));
        ctx.writeAndFlush(buf);*/

        ctx.writeAndFlush(str);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收msg消息
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 收到客户端发送的消息：" + msg);
        //通知客户端链消息发送成功
        String str = "服务端收到：" + new Date() + " " + msg + "\r\n";

        //不用再手动编码了，SocketChannel中已经添加了主动编码的方法
        /*ByteBuf buf = Unpooled.buffer(str.getBytes().length);
        buf.writeBytes(str.getBytes("GBK"));
        ctx.writeAndFlush(buf);
*/
        ctx.writeAndFlush(str);
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开链接" + ctx.channel().localAddress().toString());
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
