package com.smart.heat.netty.me.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

/**
 * @author Arnold.zhao
 * @version ClientChannelInitializer.java, v 0.1 2023-02-16 14:09 Arnold.zhao Exp $$
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        // 基于换行符号
        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));

        // 设置字符串解码，解码转String，注意调整自己的编码格式GBK、UTF-8
        socketChannel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));

        // 设置字符串编码，编码转String，注意调整自己的编码格式GBK、UTF-8
        socketChannel.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));

        // 在管道中添加我们自己的接收数据实现方法
        socketChannel.pipeline().addLast(new ClientHandler());
    }
}
