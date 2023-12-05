package com.example.ingeek21;

import com.payneteasy.tlv.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: arnold.zhao
 * @email: zhihao.zhao@ingeek.com
 * @date: 2023/12/4
 */
public class TLVTest {

    private static final Logger logger = LoggerFactory.getLogger(TLVTest.class);

    @Test
    public void tlvParser(){
        IBerTlvLogger LOG = new IBerTlvLogger() {
            @Override
            public boolean isDebugEnabled() {
                return false;
            }
            @Override
            public void debug(String s, Object... objects) {
                logger.info(s,objects);
            }
        };

        /**
         *
         * TLV 结构：
         * T 是 tag，包括一个或多个连续字节，对应的是 theTag 字段
         * L 是 length，包括一个或多个连续字节。定义的是接下来域的长度
         * V 是 value，定义对应的数值。
         *
         * 如下 Hex 是 16 进制的字符，我们要做的转换规则是将下述的16 进制字符转换位：BerTlvs 对象，
         *
         * 其中BerTlvs对象主要引用的是 BerTlv 对象，BerTlv对象是我们解析的最小单元
         *
         * 其中 BerTlv 对象中，包含：BerTag theTag; 和 byte[] theValue; 两个字段。 其中 BerTag 对象对应的主要字段也是byte[] bytes;
         *
         * 所以 BerTlv 对象的主要字段是：byte[] bytes;（theTag） 和 byte[] theValue;
         *
         * 也就是该 BerTlv 对象主要包含两个字节数组字段byte[]，分别是byte[] tag 和 byte[] value;
         *
         * 然后我们将下述的16 进制 Hex 解析为对应的BerTlv 对象，对应的parse 解析规则 则是：
         *
         * 首先由于 TLV 解析的是字节，所以我们需要先把 50045649534157131000023100000033D44122011003400000481F 这个 16 进制的字符串，先解析为对应的字节数据。
         *
         * 1 字节，1byte 等于 8 位，每一个位都只能通过 0 或1 表示，所以 1 字节对应的二进制做大结果是：11111111
         *
         * 所以此处我们把 16 进制的 50045649534157131000023100000033D44122011003400000481F 转换为字节，实际就是将该 16 进制的数据转换为 2 进制。
         *
         * 上述 16 进制的数据，转换为二进制后是：10100000000010001010110010010010101001101000001010110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
         *
         * 由于计算机中是采用的补码，补码的第一位表示正负，所以我们将上述的二进制中的前7 位 1010000 获取出来，然后加一个 0（表示正数），然后最终形成的 8 位是：01010000
         *
         * 所以上述的 16 进制数据，转换位 byte[] 字节数组后，其中 byte[0] 所对应的值就是 01010000。
         *
         * 由于 byte[0] 所对应的值是 01010000 是计算机内部关于该 1 字节所对应的 2 进制的存储关系，但是在 idea 中进行调试时，会将该 2 进制的数据转换位 10 进制展示（便于人可以清晰的看到该字节中存储的数值是什么）
         *
         * 所以在 idea 中调试的时候会看到 byte[0]对应的值是 80。也就是 01010000 所对应的 10 进制的表示方式 80.
         *
         * -------------------------------------------------------------------------------------------------------------
         * OK 了解了上述的内容之后，我们就把上述 16 进制的数据，转换为 byte[] 字节数组了。
         *
         * 然后根据该字节数组，再去解析对应的 TLV 格式。因为 TLV 格式本身就是按照字节来的。其中 T（Tag） 占用一个字节。
         *
         * 所以我们把上述的 16 进制数据，转换为 byte[] 字节数组后，数组内容如下：（以下每个字节对应的是 10 进制的表示方式）
         *
         * [80, 4, 86, 73, 83, 65, 87, 19, 16, 0, 2, 49, 0, 0, 0, 51, -44, 65, 34, 1, 16, 3, 64, 0, 0, 72, 31]
         *
         * 然后我们开始解析上述的字节数组为 TLV 格式，也就是上述的：BerTlv 对象
         *
         * 首先解析到第一位80，然后对应的则是该BerTlv对象的 theTag 的值
         *
         * 然后解析第二位 4，表示该 TLV 的V 的数据长度是后续的 4 位。
         *
         * 所以我们就获取后续的 4 位：86, 73, 83, 65 填充到对应BerTlv对象的 theValue 的字段值中。
         *
         * 那么此时我们就解析出了第一个 BerTlv 对象。
         *
         * -------------------------------------------------------------------------------------------------------------
         *
         * 然后我们接着向后解析，87, 19,  则表示该对象的 Tag 是 87，对应的Value 长度是 19.
         *
         * 此时我们解析到 Value 长度是 19 后，则把后续的 19 个字节：16, 0, 2, 49, 0, 0, 0, 51, -44, 65, 34, 1, 16, 3, 64, 0, 0, 72, 31 填充到对应的 BarTlv 的 theValue 字段中。
         *
         * 所以最终新解析出的 BarTlv 对象中的 byte[] theTag 对应的值是[87] , 对应的 byte[] theValue 字段所对应的字节数组中的值则是 [16, 0, 2, 49, 0, 0, 0, 51, -44, 65, 34, 1, 16, 3, 64, 0, 0, 72, 31]
         *
         * */

        byte[] bytes = HexUtil.parseHex("50045649534157131000023100000033D44122011003400000481F");

        BerTlvParser parser = new BerTlvParser(LOG);
        BerTlvs tlvs = parser.parse(bytes, 0, bytes.length);

        BerTlvLogger.log("    ", tlvs, LOG);
        BerTlvLogger.log("    ", tlvs, LOG);

    }

    /**
     * 以上是把 16 进制的字符，解析为 BerTlv的过程，
     *
     * 下面我们构建一个对应的 Tlv
     */

    @Test
    public void tlvBuild(){
        byte[] bytes =  new BerTlvBuilder()
                // tag 值是 0x50 （16 进制），value 值是：56495341（16 进制）
                .addHex(new BerTag(0x50), "56495341")

                // tag 值是 0x57 （16 进制），value 值是：1000023100000033D44122011003400000481F（16 进制）
                .addHex(new BerTag(0x57), "1000023100000033D44122011003400000481F")

                .buildArray();

        //构建完成后，我们直接生成对应的字节数组 byte[] ,此时我们在构建的时候，只需要填写我们的 Tag 值和对应的 Value 值即可。对应的 Length，则不需要手动输入，而是根据对应的 value 长度自动计算出对应的 Length 值。

        //所以我们此处将该二进制数组转换为对应的 16 进制数据：转换后的结果则是：50045649534157131000023100000033D44122011003400000481F

        //我们此处仔细看下生成的 16 进制的值：50 04 56495341，其中50 是我们设置的 16 进制的 tag 0x50，而56495341则是我们设置的 value。另外还多了一个 04 这里的 04 则是buildArray()的时候，自动根据对应的 value 长度进行计算的值 04。所以 length 是我不需要我们自定义的，只需要定义对应的 Tag 和 value 值即可。

        //字节数组转换 为 16 进制
        String hex =  HexUtil.toHexString(bytes);

        //转换后 hex 结果是：50045649534157131000023100000033D44122011003400000481F
        System.out.println(hex);

        BerTag berTag = new BerTag(0x50);
        BerTag berTag1 = new BerTag(0x50);

        System.out.println(berTag.hashCode());
        System.out.println(berTag1.hashCode());

    }


}
