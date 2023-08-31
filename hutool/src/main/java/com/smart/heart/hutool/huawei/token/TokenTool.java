package com.smart.heart.hutool.huawei.token;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.smart.heart.hutool.huawei.constant.SystemConstant;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 华为云Token工具类
 * @Author: Arnold.zhao
 * @Date: 2020/12/28
 */
public class TokenTool {

    private static Map<String, String> tokenMap = new ConcurrentHashMap<>();

    private static void createToken() throws Exception {

        String json = BuildAuth.create().
                buildIdentity(SystemConstant.HUAWEI_USER_ACCOUNT, SystemConstant.HUAWEI_IAM_USER_ACCOUNT, SystemConstant.HUAWEI_IAM_PASSWORD)
                .toJSONString();

        HttpResponse httpResponse = HttpRequest.post(SystemConstant.HUAWEI_TOKEN_URL)
                .header(Header.CONTENT_TYPE, "application/json;charset=utf8")
                .body(json).execute();

        if (httpResponse.isOk()) {
            String token = httpResponse.headers().get(SystemConstant.HUAWEI_HEADER_TOKEN).get(0);
            tokenMap.put(SystemConstant.HUAWEI_HEADER_TOKEN, token);
            //执行定时器，23 小时后清除token记录，重新获取token
            referToken();
        } else {
            throw new Exception(httpResponse.getStatus() + " - 华为云token生成失败");
        }

    }

    public static String getToken() throws Exception {
        String token = tokenMap.get(SystemConstant.HUAWEI_HEADER_TOKEN);
        if (token != null) {
            return token;
        } else {
            createToken();
            return getToken();
        }
    }

    private static void referToken() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                //map 中无需remove 原有Token，直接生成并替换
                createToken();
            }
        }, SystemConstant.HUAWEI_REFER_TIME_TOKEN);
    }


    public static void main(String[] args) throws Exception {

        System.out.println(getToken());
        /*Thread.sleep(3000);
        createToken();
        System.out.println("*********");
        Thread.sleep(3000);
        createToken();*/
    }
}
