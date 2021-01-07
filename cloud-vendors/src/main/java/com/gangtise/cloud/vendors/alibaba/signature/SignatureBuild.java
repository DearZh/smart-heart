package com.gangtise.cloud.vendors.alibaba.signature;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.gangtise.cloud.vendors.alibaba.constant.AlibabaConstant;
import com.gangtise.cloud.vendors.alibaba.constant.HttpMethod;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用方式：SignatureBuild.GET().URL(map);
 *
 * @Description: 构建阿里的接口签名
 * @Author: Arnold.zhao
 * @Date: 2021/1/6
 */
public class SignatureBuild {

    private HttpMethod httpMethod;
    private Map<String, String> parameters;
    private Map<String, String> encodeParameters;

    private String signature;


    public static Get GET() {
        SignatureBuild signatureBuild = new SignatureBuild();
        signatureBuild.httpMethod = HttpMethod.GET;
        signatureBuild.parameters = new ConcurrentHashMap<>();
        signatureBuild.encodeParameters = new ConcurrentHashMap<>();
        Get get = signatureBuild.new Get();
        get.signatureBuild = signatureBuild;
        return get;
    }

    /**
     * 生成待签名计算符 stringToSign 的HMAC值
     *
     * @param encryptText stringToSign
     * @param encryptKey  AccessKey Secret
     * @return
     * @throws Exception
     */
    private static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data = encryptKey.getBytes(AlibabaConstant.ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, AlibabaConstant.MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(AlibabaConstant.MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(AlibabaConstant.ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }

    /**
     * 接口请求参数和值分别采用UTF-8的字符集进行URL编码
     *
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, AlibabaConstant.ENCODING).replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;
    }

    /**
     * 公共请求参数Timestamp的值生成，使用UTC时间按照 ISO8601标准生成对应的时间戳
     *
     * @param date
     * @return
     */
    private static String formatIso8601Date(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(AlibabaConstant.ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }


    /**
     * 构建请求参数
     *
     * @param originParameter 接口自定义请求参数
     * @return
     * @throws Exception
     */
    private SignatureBuild parameter(Map<String, String> originParameter) throws Exception {
        //构建公共请求参数
        String timestamp = formatIso8601Date(new Date());
        String signatureNonce = UUID.randomUUID().toString();
        String signatureVersion = "1.0";

        parameters.put("Version", "2020-03-26");
        parameters.put("AccessKeyId", AlibabaConstant.ACCESS_KEY_ID);
        parameters.put("Timestamp", timestamp);
        parameters.put("SignatureMethod", "HMAC-SHA1");
        parameters.put("SignatureVersion", signatureVersion);
        parameters.put("SignatureNonce", signatureNonce);
        parameters.put("Format", "JSON");
        //putAll 自定义接口参数
        parameters.putAll(originParameter);
        return this;
    }


    /**
     * 构建签名
     *
     * @return
     * @throws Exception
     */
    private SignatureBuild stringToSign() throws Exception {
        //排序请求参数 (按照参数名称的字典顺序对请求参数进行排序)
        String[] sortedKeys = parameters.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);

        StringBuilder canonicalizedQueryString = new StringBuilder();
        //构造待签名字符串          使用 & 符号链接，得到规范化的请求字符串
        for (String key : sortedKeys) {
            String encodeKey = percentEncode(key);
            String encodValue = percentEncode(parameters.get(key));
            canonicalizedQueryString.append("&")
                    .append(encodeKey).append("=")
                    .append(encodValue);
            encodeParameters.put(encodeKey, encodValue);
        }

        // 构造 stringToSign 字符串
        final String SEPARATOR = "&";

        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(httpMethod).append(SEPARATOR);
        stringToSign.append(percentEncode("/")).append(SEPARATOR);
        stringToSign.append(percentEncode(
                canonicalizedQueryString.toString().substring(1)));

        //采用HMAC计算签名值
        byte[] signData = HmacSHA1Encrypt(stringToSign.toString(), AlibabaConstant.ACCESS_KEY_SECRET);
        //Base64重新编码后得到最终的签名值
        signature = Base64.getEncoder().encodeToString(signData);

        return this;
    }


    /**
     * 返回签名及参数编码后的URL路径
     *
     * @return
     * @throws Exception
     */
    private String URL() throws Exception {
        //将对应的签名填充至map中
        encodeParameters.put(AlibabaConstant.SIGNATURE, signature);

        StringBuffer urlParameter = new StringBuffer();
        for (String key : encodeParameters.keySet()) {
            urlParameter.append("&")
                    .append(key).append("=")
                    .append(encodeParameters.get(key));
        }
        String url = AlibabaConstant.ENDPOINT + "?" + urlParameter.toString().substring(1);

        return url;
    }

    public String URL(Map<String, String> originParameter) throws Exception {
        return this.parameter(originParameter).stringToSign().URL();
    }

    /**
     * @Description: 单独抽出Get签名类 便于后续扩展构建Post等签名
     * @Author: Arnold.zhao
     * @Date: 2021/1/6
     */
    private class Get {
        private SignatureBuild signatureBuild;

        public String URL(Map<String, String> originParameter) throws Exception {
            return signatureBuild.parameter(originParameter).stringToSign().URL();
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            //测试获取产品类别接口 Arnold.zhao 2021/1/6
            Map<String, String> map = new HashMap<>();
            //工单查询类型接口
//        map.put(AlibabaConstant.ACTION, "ListProducts");
            //ECS查询地域接口
            map.put(AlibabaConstant.ACTION, "DescribeRegions");
            map.put("Version", "2014-05-26");
            String url = SignatureBuild.GET().URL(map);
            System.out.println(url);
            HttpResponse response = HttpRequest.get(url).execute();
            System.out.println(response.isOk());
            System.out.println(response.body());
        }
    }
}
