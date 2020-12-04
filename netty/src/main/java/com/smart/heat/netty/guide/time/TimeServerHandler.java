package com.smart.heat.netty.guide.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/11/14
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        //buf.readableBytes() 获取缓冲区可读的字节数；
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8");
        System.out.println("the time server receive order:" + body);
        //所接受到的body内容如果为query，则返回对应的消息，否则则返回DD；
        String currentTime = "query time order".equals(body) ? new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) : "DD";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        //ctx.write() 为异步方法，发送应答消息给到客户端；
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
