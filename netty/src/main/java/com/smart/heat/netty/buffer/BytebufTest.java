package com.smart.heat.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

/**
 * @author Arnold.zhao
 * @version BytebufTest.java, v 0.1 2022-09-29 17:35 Arnold.zhao Exp $$
 */
public class BytebufTest {
    public static void main(String[] args) {

        ByteBuf buf = Unpooled.buffer(15);
        String content = "ytao中文测c";
        buf.writeBytes(content.getBytes());
        System.out.println(String.format("\nwrite: ridx=%s widx=%s cap=%s redInt=%s", buf.readerIndex(), buf.writerIndex(), buf.capacity(), buf.readInt()));

        byte[] dst = new byte[4];
        buf.readBytes(dst);

        System.out.println(String.format("\nread(4): ridx=%s widx=%s cap=%s", buf.readerIndex(), buf.writerIndex(), buf.capacity()));

        System.out.println(new String(dst));
        buf.readBytes(dst);
        System.out.println(new String(dst));
        System.out.println(String.format("\nread(4): ridx=%s widx=%s cap=%s", buf.readerIndex(), buf.writerIndex(), buf.capacity()));

        int a = buf.indexOf(0, 15, (byte) 'c');
        System.out.println(a);


        int c = buf.indexOf(0, 15, (byte) 111);
        System.out.println(c);

        System.out.println(((char) 67));

        byte bb = (byte) -143;

        System.out.println(new String(String.valueOf(bb)));
        System.out.println(Integer.toBinaryString(-143));
        System.out.println(Integer.toBinaryString(113));

        byte cc = (byte) 120;
        System.out.println(String.valueOf(cc));

        /**
         * 直接对应ASCII对照表，进行数值和字符的对应关系 https://tool.oschina.net/commons?type=4
         * 输出：y
         */
        char ca = 121;
        System.out.println(ca);

        /**
         * system这里方法输出，默认是输出的int类型， 所以此处是会将该byte字节转换为int,在Java中，所有整型字面值都是int型，所有小数都是double型。
         * 此处输出结果为：121
         */
        byte dds = 'y';
        System.out.println(dds);


        ByteBuf buf1 = Unpooled.buffer(15);
        System.out.println(buf1.writerIndex());
        buf1.writeBytes(new byte[]{'\t'});
        System.out.println(buf1.writerIndex());
        System.out.println(">>>>>>>>>>>>");

        System.out.println('\t');//这里输出的是一个char
        System.out.println('\f');
        byte t = '\t';
        System.out.println(t);//这里输出的是int，结果是9
        //这里的 \t 对应的是ASCII值的控制字符，该 \t 对应的是控制字符 HT，对应的ASCII值是 9
        byte ts = '*';
        System.out.println(ts);//输出控制字符，此处对应的是ASCII的值 42.

        char ssd = '繁';
        System.out.println(ssd);
        System.out.println("繁".getBytes().length);
        byte wewe = '\t';
        System.out.println(wewe);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>");

        init();
    }

    public static void init() {
        Unpooled.buffer();//byte[] 内存
        Unpooled.directBuffer();//直接内存
        Unpooled.compositeBuffer();
        Unpooled.wrappedBuffer(new byte[2]);

        //JAVA NIO API ; https://juejin.cn/post/6844904095786991623

        //堆内存，直接内存（DirectByteBuffer），内存映射
        ByteBuffer.allocateDirect(1024);//底层使用 unsafe.allocateMemory()分配直接内存
        //MappedByteBuffer  内存映射（内存的变动会直接更新文件，linux来进行维护）

        //堆外内存的监控工具: https://heapdump.cn/article/2906673
        //关于对外直接内存的回收：https://www.jianshu.com/p/7504e2cbe8db
        //堆外内存，直接映射内存区别：https://juejin.cn/post/6844904095786991623
        //
        
    }

}
