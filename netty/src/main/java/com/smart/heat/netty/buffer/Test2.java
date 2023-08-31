package com.smart.heat.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arnold.zhao
 * @version Test2.java, v 0.1 2022-09-26 17:59 Arnold.zhao Exp $$
 */
public class Test2 {
    public static void main(String[] args) {
        for (int i = 0; i < 1000000000; i++) {
            /*ByteBuf buf = Unpooled.buffer(15);
            Unpooled.directBuffer(20);
            */
            byte[] b = new byte[1024 * 1024 * 1024];
            ByteBuffer m_buf = ByteBuffer.wrap(b);
//            m_buf.clear();
//            m_buf.reset();
            System.out.println(">>>>>>>>>>.");
        }
    }

    /**
     * 我是根据一个编号查询数据 然后查询不到的话把这个编号放到redis里
     * 现在要判断这个编号3次查不到的话就不存redis里了
     */
    static Map<String, Integer> map = new HashMap<>();

    /*public static void test() {
        String num = "编号";//待查询的编号
        //执行查询逻辑，判断这个编号是否存在，不存在，则添加到map中
        if (不存在) {
            map.put(num,1);
        }
        //如果第二次执行查询逻辑还不存在，就map 的value +1
        map.put(num,map.get(num)+1);
        //如果第三次执行查询逻辑还不存在，就map 的value +1
        map.put(num,map.get(num)+1);
        //如果最终该查询编号，所对应的value，是3，则放到redis里面
        if(map.get(num)>=3){
            //存放到redis中
        }
    }
    */
}
