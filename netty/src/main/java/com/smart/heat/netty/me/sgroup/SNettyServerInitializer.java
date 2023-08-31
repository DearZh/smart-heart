package com.smart.heat.netty.me.sgroup;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

/**
 * @author Arnold.zhao
 * @version SNettyServerInitializer.java, v 0.1 2023-02-16 13:58 Arnold.zhao Exp $$
 */
public class SNettyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //netty channel pipeline,获取netty socket管道流，在对应的管道流中增加一系列处理方法。
        ChannelPipeline p = socketChannel.pipeline();

        //基于长度进行解码（缓冲区拿到10个字符的长度数据数据后，才会去执行channelRead()方法，如果没有拿到则等待拿到10个后才会执行处理器的channelRead()方法）
//       p.addLast(new FixedLengthFrameDecoder(10));

        //基于换行进行解码，只要接受到的文本后面带 \n 则会执行一次channelRead()方法,如果一直没有接收到\n，则表示一行数据一直没有结束，则一直等待\n信号后，才会调用对应处理器方法
        p.addLast(new LineBasedFrameDecoder(1024));

        //设置字符串解码，这样收取数据时就不用再手动解码了
        p.addLast(new StringDecoder(Charset.forName("GBK")));

        //设置字符串编码，这样在后续的处理器中直接发送数据时候，就不用再进行编码了，直接传输字符即可。注意调整自己的编码格式GBK、UTF-8
        p.addLast(new StringEncoder(Charset.forName("GBK")));

        //调试输出日志时使用、真实环境是不会开启的
        p.addLast(new LoggingHandler(LogLevel.INFO));

        //设置对应的处理器，每次初始化通道均实例化一个该新的处理器。如果直接指定static变量 serverHandler，则会导致链接后再断开，再进行客户端链接会出现异常提示。
        p.addLast(new SNettyServerHandler());
    }
}
