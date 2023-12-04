package com.example.ingeek21;


import org.junit.jupiter.api.Test;
import com.payneteasy.tlv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: arnold.zhao
 * @email: zhihao.zhao@ingeek.com
 * @date: 2023/12/4
 */
public class BerTlvParserTest {

    private static final Logger logger = LoggerFactory.getLogger(BerTlvParserTest.class);


    public static final BerTag TAG_DF0D_ID = new BerTag(0xdf, 0x0d);
    public static final BerTag TAG_DF7F_VERSION = new BerTag(0xdf, 0x7f);

    private static final IBerTlvLogger LOG = new IBerTlvLogger() {
        @Override
        public boolean isDebugEnabled() {
            return false;
        }
        @Override
        public void debug(String s, Object... objects) {
            logger.info(s,objects);
        }
    };

    public static final BerTag T_EF = new BerTag(0xEF);


    @Test
    public void testParse() {

        String hex =
                /*            0  1  2  3   4  5  6  7     8  9  a  b   c  d  e  f      0123 4567  89ab  cdef */
                /*    0 */   "50 04 56 49  53 41 57 13    10 00 02 31  00 00 00 33" // P.VI SAW.  ...1  ...3
                /*   10 */ + "d4 41 22 01  10 03 40 00    00 48 1f 5a  08 10 00 02" // .A". ..@.  .H.Z  ....
                /*   20 */ + "31 00 00 00  33 5f 20 1a    43 41 52 44  33 2f 45 4d" // 1... 3_ .  CARD  3/EM
                /*   30 */ + "56 20 20 20  20 20 20 20    20 20 20 20  20 20 20 20" // V
                /*   40 */ + "20 20 5f 24  03 44 12 31    5f 28 02 06  43 5f 2a 02" //   _$ .D.1  _(..  C_*.
                /*   50 */ + "06 43 5f 30  02 02 01 5f    34 01 06 82  02 5c 00 84" // .C_0 ..._  4...  .\..
                /*   60 */ + "07 a0 00 00  00 03 10 10    95 05 40 80  00 80 00 9a" // .... ....  ..@.  ....
                /*   70 */ + "03 14 02 10  9b 02 e8 00    9c 01 00 9f  02 06 00 00" // .... ....  ....  ....
                /*   80 */ + "00 03 01 04  9f 03 06 00    00 00 00 00  00 9f 06 07" // .... ....  ....  ....
                /*   90 */ + "a0 00 00 00  03 10 10 9f    09 02 00 8c  9f 10 07 06" // .... ....  ....  ....
                /*   a0 */ + "01 0a 03 a0  a1 00 9f 1a    02 08 26 9f  1c 08 30 36" // .... ....  ..&.  ..06
                /*   b0 */ + "30 34 35 33  39 30 9f 1e    08 30 36 30  34 35 33 39" // 0453 90..  .060  4539
                /*   c0 */ + "30 9f 26 08  cb fc 76 79    77 11 1f 15  9f 27 01 80" // 0.&. ..vy  w...  .'..
                /*   d0 */ + "9f 33 03 e0  b8 c8 9f 34    03 5e 03 00  9f 35 01 22" // .3.. ...4  .^..  .5."
                /*   e0 */ + "9f 36 02 00  0e 9f 37 04    46 1b da 7c  9f 41 04 00" // .6.. ..7.  F..|  .A..
                /*   f0 */ + "00 00 63                                            " // ..c
                ;
        BerTlvs tlvs =  parse(hex);
        byte[] bytes = HexUtil.parseHex(hex);
        System.out.println(tlvs);
        System.out.println(tlvs);

    }

    private BerTlvs parse(String hex) {
        byte[] bytes = HexUtil.parseHex(hex);

        BerTlvParser parser = new BerTlvParser(LOG);
        BerTlvs tlvs = parser.parse(bytes, 0, bytes.length);
        BerTlvLogger.log("    ", tlvs, LOG);

        return tlvs;
    }

    @Test
    public void testParseLen2() {
        String hex =
                /*            0  1  2  3   4  5  6  7     8  9  a  b   c  d  e  f      0123 4567  89ab  cdef */
                /*    0 */   "e0 81 91 71  7f 9f 18 04    12 34 56 78  86 0d 84 24" // ...q ....  .4Vx  ...$
                /*   10 */ + "00 00 08 5a  e9 57 e8 81    8d 95 a8 86  0d 84 24 00" // ...Z .W..  ....  ..$.
                /*   20 */ + "00 08 36 d2  44 95 47 47    ec 1e 86 0d  84 24 00 00" // ..6. D.GG  ....  .$..
                /*   30 */ + "08 38 63 b1  c1 79 be 38    ac 86 0d 84  24 00 00 08" // .8c. .y.8  ....  $...
                /*   40 */ + "25 4d b4 b4  ec db 21 74    86 0d 84 24  00 00 08 67" // %M.. ..!t  ...$  ...g
                /*   50 */ + "8d f9 12 84  78 e2 8f 86    0d 84 24 00  00 08 51 4c" // .... x...  ..$.  ..QL
                /*   60 */ + "8f d9 5a 21  6c 0b 86 0d    84 24 00 00  08 1f 62 34" // ..Z! l...  .$..  ..b4
                /*   70 */ + "65 db 0c 95  59 86 0d 84    24 00 00 08  2a 5a 0a 9a" // e... Y...  $...  *Z..
                /*   80 */ + "82 8a ba 0a  91 0a 42 50    0e 43 81 a4  67 7a 30 30" // .... ..BP  .C..  gz00
                /*   90 */ + "8a 02 30 30                                         " // ..00
                ;
        parse(hex);
    }


    @Test
    public void testMulti() {
        String hex =
                /*            0  1  2  3   4  5  6  7     8  9  a  b   c  d  e  f      0123 4567  89ab  cdef */
                /*    0 */   "e1 35 9f 1e  08 31 36 30    32 31 34 33  37 ef 12 df" // .5.. .160  2143  7...
                /*   10 */ + "0d 08 4d 30  30 30 2d 4d    50 49 df 7f  04 31 2d 32" // ..M0 00-M  PI..  .1-2
                /*   20 */ + "32 ef 14 df  0d 0b 4d 30    30 30 2d 54  45 53 54 4f" // 2... ..M0  00-T  ESTO
                /*   30 */ + "53 df 7f 03  36 2d 35                               " // S... 6-5
                ;
        BerTlvs tlvs = parse(hex);
//        org.springframework.util.//Assert.
        //Assert.//AssertNotNull(tlvs.find(new BerTag(0xe1)));
        //Assert.//AssertEquals("1 6 0 2 1 4 3 7".replace(" ", ""), tlvs.find(new BerTag(0x9f, 0x1e)).getTextValue());
        //Assert.//AssertNotNull(tlvs.find(T_EF));
        //Assert.//AssertEquals(2, tlvs.findAll(T_EF).size());

        // first EF
        {
            BerTlv firstEf = tlvs.find(T_EF);
            //Assert.//AssertNotNull(firstEf);
            //Assert.//AssertNotNull(firstEf.find(TAG_DF0D_ID));
            //Assert.//AssertEquals("M000-MPI", firstEf.find(TAG_DF0D_ID).getTextValue());
            //Assert.//AssertNotNull("No EF / DF7F tag found", firstEf.find(TAG_DF7F_VERSION));
            //Assert.//AssertEquals("1-22", firstEf.find(TAG_DF7F_VERSION).getTextValue());
        }

        // second EF
        BerTlv secondEf = tlvs.findAll(T_EF).get(1);
        //Assert.//AssertNotNull(secondEf);
        //Assert.//AssertNotNull(secondEf.find(TAG_DF0D_ID));
        //Assert.//AssertEquals("M000-TESTOS", secondEf.find(TAG_DF0D_ID).getTextValue());
        //Assert.//AssertNotNull("No EF / DF7F tag found", secondEf.find(TAG_DF7F_VERSION));
        //Assert.//AssertEquals("6-5", secondEf.find(TAG_DF7F_VERSION).getTextValue());


    }

    @Test
    public void test_empty_length() {
        BerTlv tlv = new BerTlvParser(LOG).parseConstructed(HexUtil.parseHex("E3 02 01 00"));
        BerTlvLogger.log("    ", tlv, LOG);

        //Assert.//AssertEquals(tlv.getTag(), new BerTag(0xe3));
        //Assert.//AssertNotNull(tlv.getValues());
        //Assert.//AssertEquals(1, tlv.getValues().size());
        BerTlv emptyTag = tlv.getValues().get(0);
        //Assert.//AssertEquals(new BerTag(0x01), emptyTag.getTag());
        //Assert.//AssertEquals("", emptyTag.getHexValue());
    }

    @Test
    public void test_empty_hex() {
        byte[] bytes = HexUtil.parseHex("");
        BerTlvParser parser = new BerTlvParser(LOG);
        BerTlvs tlvs = parser.parse(bytes, 0, bytes.length);
        //Assert.//AssertEquals(0, tlvs.getList().size());
    }

    @Test
    public void test_issue_10_last_tag() {
        String hex = "BF01820114DF0105A0000000049F2201EFDF020101DF030101DF0481F8A191CB87473F29349B5D60A88B3EAEE0973AA6F1A082F358D849FDDFF9C091F899EDA9792CAF09EF28F5D22404B88A2293EEBBC1949C43BEA4D60CFD879A1539544E09E0F09F60F065B2BF2A13ECC705F3D468B9D33AE77AD9D3F19CA40F23DCF5EB7C04DC8F69EBA565B1EBCB4686CD274785530FF6F6E9EE43AA43FDB02CE00DAEC15C7B8FD6A9B394BABA419D3F6DC85E16569BE8E76989688EFEA2DF22FF7D35C043338DEAA982A02B866DE5328519EBBCD6F03CDD686673847F84DB651AB86C28CF1462562C577B853564A290C8556D818531268D25CC98A4CC6A0BDFFFDA2DCCA3A94C998559E307FDDF915006D9A987B07DDAEB3BDF050103";
        byte[] bytes = HexUtil.parseHex(hex);
        BerTlvParser parser = new BerTlvParser();
        BerTlvs tlvs = parser.parse(bytes, 0, bytes.length);

        BerTlvLogger.log("  ", tlvs, LOG);

        {
            BerTlv berTlv = tlvs.find(new BerTag(0xDF, 0x05));
            //Assert.//AssertNotNull(berTlv);
            //Assert.//AssertEquals("03", berTlv.getHexValue());
        }

        {
            //Assert.//AssertEquals(1, tlvs.getList().size());
            //Assert.//AssertTrue(tlvs.getList().get(0).isConstructed());
            //Assert.//AssertEquals(6, tlvs.getList().get(0).getValues().size());
        }
    }
}
