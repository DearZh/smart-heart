package com.smart.heart.hutool;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/12/26
 */
public class HttpTest {

    public static void main(String[] args) {
        String content = HttpUtil.get("https://www.baidu.com");
        System.out.println(content);

        HttpRequest.post("").header(Header.CONTENT_TYPE,"");
    }
}
