package com.smart.heat.netty.guide.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;
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
        //获取客户端发送过来的消息
        ByteBuf buf = (ByteBuf) msg;
        //buf.readableBytes() 获取缓冲区可读的字节数；
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = null;
        try {
            body = new String(req, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("服务器收到消息:" + body);
        //所接受到的body内容如果为 query，则返回对应的消息，否则则返回DD；
        String currentTime = "query time order".equals(body) ? new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) : "指令错误,不返回消息";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        //ctx.write() 为异步方法，发送应答消息给到客户端；
        ctx.write(resp);
        //更改为异步处理
        //更改为异步执行后，消息发出。。客户端收不到。。
        /*ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {

            }
        });*/


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
