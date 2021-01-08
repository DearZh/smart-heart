package com.gangtise.cloud.vendors.alibaba;

import com.gangtise.cloud.vendors.alibaba.constant.AlibabaConstant;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/6
 */
public class Test {
    private static final String ENCODING = "UTF-8";
    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";


    /* public static void main(String[] args) throws UnsupportedEncodingException {
         String paramter = "你好ABCabc012 AA";
         System.out.println(percentEncode(paramter));




 //        Signature = Base64( HmacSHA1Encrypt( AccessSecret, UTF-8-Encoding-Of(paramter) ) )

     }*/
    private static final String MAC_NAME = "HmacSHA1";

    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);//OK
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME); //OK
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }

    private static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, ENCODING).replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;
    }

    private static String formatIso8601Date(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        final String HTTP_METHOD = "GET";
        Map<String, String> parameters = new HashMap();
// 输入请求参数(公共参数）
        parameters.put("Action", "ListProducts");
        parameters.put("Version", "2020-03-26");
        parameters.put("AccessKeyId", AlibabaConstant.ACCESS_KEY_ID);
        String time = formatIso8601Date(new Date());
        System.out.println("time：" + time);
        System.out.println("time：" + percentEncode(time));
        parameters.put("Timestamp", time);
        parameters.put("SignatureMethod", "HMAC-SHA1");
        parameters.put("SignatureVersion", "1.0");
        String uuid = UUID.randomUUID().toString();
        System.out.println("UUID：" + uuid);
        parameters.put("SignatureNonce", uuid);
        parameters.put("Format", "JSON");
        //排序请求参数
        String[] sortedKeys = parameters.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);
        for (int i = 0; i < sortedKeys.length; i++) {
            System.out.println(sortedKeys[i]);
        }
        StringBuilder canonicalizedQueryString = new StringBuilder();
        //构造待签名字符串          使用 & 符号链接，得到规范化的请求字符串
        for (String key : sortedKeys) {
// 这里注意编码 key 和 value
            canonicalizedQueryString.append("&")
                    .append(percentEncode(key)).append("=")
                    .append(percentEncode(parameters.get(key)));
        }
        System.out.println("待签名字符串：" + canonicalizedQueryString);

        // 构造 stringToSign 字符串
        final String SEPARATOR = "&";
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(HTTP_METHOD).append(SEPARATOR);
        stringToSign.append(percentEncode("/")).append(SEPARATOR);
        stringToSign.append(percentEncode(
                canonicalizedQueryString.toString().substring(1)));

        System.out.println("构造 签名字符串：" + stringToSign);

        //步骤2，计算签名值
        // 以下是一段计算签名的示例代码
//        String key = "KRSWBB7cSiRgXAdLkrk4c5WkpaieoT&";
        /*final String ALGORITHM = "HmacSHA1";
        final String ENCODING = "UTF-8";
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM));

        byte[] signData = mac.doFinal(stringToSign.getBytes(ENCODING));
        */

        byte[] signData = HmacSHA1Encrypt(stringToSign.toString(), AlibabaConstant.ACCESS_KEY_SECRET);

        String signature = new String(Base64.getEncoder().encodeToString(signData));

        System.out.println("签名signature：" + signature);
        //https://help.aliyun.com/document_detail/25492.html?spm=5176.2020520129.0.dexternal.643c46aebtA4VN
    }
}
