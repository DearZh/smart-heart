package com.smart.heat.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 验证字节
 * @author Arnold.zhao
 * @version Test.java, v 0.1 2022-09-23 17:54 Arnold.zhao Exp $$
 */
public class Test {
    public static void main(String[] args) {

        ByteBuf buf = Unpooled.buffer(8 * 1024);
        buf.writeBytes("system out 你".getBytes());

        byte[] b = new byte[10];
        byte[] s = "你".getBytes();//中文默认3字节
        System.out.println(s.length);
        System.out.println(buf.indexOf(0, 10, s[0]));

        System.out.println("查看当前JVM默认运行的编码集");
        //查看当前JVM默认运行的编码集
        System.out.println(Charset.defaultCharset());

        //确认中文在各个编码字符集下所占用的字节长度
        System.out.println("确认中文在各个编码字符集下所占用的字节长度");
        String str = "你";
        System.out.println(str.getBytes(StandardCharsets.UTF_8).length);//3
        System.out.println(str.getBytes(StandardCharsets.UTF_16).length);//4
        try {
            System.out.println(str.getBytes("GBK").length);//2
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        //英文在各个环境下的所占字节长度
        System.out.println("英文在各个环境下的所占字节长度");
        String strA = "a";
        System.out.println(strA.getBytes(StandardCharsets.UTF_8).length);//1
        System.out.println(strA.getBytes(StandardCharsets.UTF_16).length);//4
        try {
            System.out.println(strA.getBytes("GBK").length);//1
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //数字在各个字符编码集下所占用字节长度
        System.out.println("数字在各个字符编码集下所占用字节长度");
        Integer intA = 9999;

        byte a = intA.byteValue();
        System.out.println(a);
        /**
         * 一个byte（字节）是8位，二进制补码表示是-128 和 127
         *
         */
        byte bd = (byte) 48567377;
        int we = bd;
        System.out.println(we);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        System.out.println(intA.toString().getBytes(StandardCharsets.UTF_8).length);
        System.out.println(intA.toString().getBytes(StandardCharsets.UTF_16).length);
        try {
            System.out.println(intA.toString().getBytes("GBK").length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
