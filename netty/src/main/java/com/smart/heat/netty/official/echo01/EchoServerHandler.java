package com.smart.heat.netty.official.echo01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Handler implementation for the echo01 server.
 */

/**
 * 回显服务器的处理程序实现
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(">>>>>>>channelActive>>>>>>>>");
        ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.write("It is " + new Date() + " now.\r\n");
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

//        ctx.write(msg);

        //获取客户端发送过来的消息
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println("收到客户端" + ctx.channel().remoteAddress() + "发送的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("收到客户端" + ctx.channel().remoteAddress() + "发送的消息：" + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

//        ctx.flush();

        //发送消息给客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("server success and return", CharsetUtil.UTF_8));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 引发异常时关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
