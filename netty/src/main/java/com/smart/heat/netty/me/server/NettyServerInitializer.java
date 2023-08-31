package com.smart.heat.netty.me.server;

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
 * @version NettyServerInitializer.java, v 0.1 2023-02-16 10:37 Arnold.zhao Exp $$
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final NettyServerHandler serverHandler = new NettyServerHandler();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //netty channel pipeline,获取netty socket管道流，在对应的管道流中增加一系列处理方法。
        ChannelPipeline p = socketChannel.pipeline();

        //基于长度进行解码（缓冲区拿到10个字符的长度数据数据后，才会去执行channelRead()方法，如果没有拿到则等待拿到10个后才会执行处理器的channelRead()方法）
//       p.addLast(new FixedLengthFrameDecoder(10));

        //基于换行进行解码，只要接受到的文本后面带 \n 则会执行一次channelRead()方法,如果一直没有接收到\n，则表示一行数据一直没有结束，则一直等待\n信号后，才会调用对应处理器方法
        //虽然是基于换行符，但还是要设置一次解码的最大长度，此处是1024，超出则会抛出异常 io.netty.handler.codec.TooLongFrameException: frame length (6931) exceeds the allowed maximum (1024)
        p.addLast(new LineBasedFrameDecoder(9000));

        //https://bugstack.cn/md/netty/base/2019-08-12-netty%E6%A1%88%E4%BE%8B%EF%BC%8Cnetty4.1%E5%9F%BA%E7%A1%80%E5%85%A5%E9%97%A8%E7%AF%87%E4%B9%9D%E3%80%8A%E8%87%AA%E5%AE%9A%E4%B9%89%E7%BC%96%E7%A0%81%E8%A7%A3%E7%A0%81%E5%99%A8%EF%BC%8C%E5%A4%84%E7%90%86%E5%8D%8A%E5%8C%85%E3%80%81%E7%B2%98%E5%8C%85%E6%95%B0%E6%8D%AE%E3%80%8B.html
        //设置字符串解码，这样收取数据时就不用再手动解码了。可以通过自定义一个解码器，设置自己的解码规则。
        p.addLast(new StringDecoder(Charset.forName("GBK")));

        //设置字符串编码，这样在后续的处理器中直接发送数据时候，就不用再进行编码了，直接传输字符即可。注意调整自己的编码格式GBK、UTF-8
        p.addLast(new StringEncoder(Charset.forName("GBK")));

        //调试输出日志时使用、真实环境是不会开启的
        p.addLast(new LoggingHandler(LogLevel.INFO));

        //设置对应的处理器，每次初始化通道均实例化一个该新的处理器。如果直接指定static变量 serverHandler，则会导致链接后再断开，再进行客户端链接会出现异常提示。
        p.addLast(new NettyServerHandler());

        /**
         * https://bugstack.cn/md/netty/base/2019-08-13-netty%E6%A1%88%E4%BE%8B%EF%BC%8Cnetty4.1%E5%9F%BA%E7%A1%80%E5%85%A5%E9%97%A8%E7%AF%87%E5%8D%81%E3%80%8A%E5%85%B3%E4%BA%8EChannelOutboundHandlerAdapter%E7%AE%80%E5%8D%95%E4%BD%BF%E7%94%A8%E3%80%8B.html
         * 当客户端连接到服务器时，Netty新建一个ChannelPipeline处理其中的事件，而一个ChannelPipeline中含有若干ChannelHandler
         * 如果每个客户端连接都新建一个ChannelHandler实例，当有大量客户端时，服务器将保存大量的ChannelHandler实例。
         * 为此，Netty提供了Sharable注解，如果一个ChannelHandler状态无关，那么可将其标注为Sharable，如此，服务器只需保存一个实例就能处理所有客户端的事件。
         *
         * 所以，此处针对serverHandler类，只需要添加@Sharable注解即可，也可以避免上述提到的断开连接再打开异常的问题。
         */
//        p.addLast(serverHandler);
    }
}
