package com.smart.heat.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/11/14
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private final ByteBuf firstMessage;

    public TimeClientHandler() {
        byte[] req = "query time order".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        this.firstMessage.writeBytes(req);
    }

    /**
     * 当客户端和服务端TCP链路连接成功后，Netty的NIO线程会调用channelActive()方法，发送查询时间的指令给服务端，
     * 使用writeAndFlush()方法，将请求消息发送给服务端；
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }

    /**
     * 当服务端返回应答消息时，channelRead()将会被调用；
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8");
        System.out.println("TimeClientHandler no is :" + body);
        ctx.write("你好啊");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("TimeClientHandler 释放资源 ");
        ctx.close();
    }
}
