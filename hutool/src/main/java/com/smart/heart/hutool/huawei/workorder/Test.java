package com.smart.heart.hutool.huawei.workorder;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.smart.heart.hutool.huawei.constant.SystemConstant;
import com.smart.heart.hutool.huawei.token.TokenTool;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/12/28
 */
public class Test {
    public static void main(String[] args) throws Exception {
        String token = TokenTool.getToken();
        System.out.println(token);
        //查询工单列表
        HttpResponse httpResponse = HttpRequest.get(SystemConstant.HUAWEI_CASE_URL + SystemConstant.HUAWEI_CASE_LIST)
                .header(SystemConstant.HUAWEI_REQUEST_TOKEN, token).execute();

        System.out.println(httpResponse.isOk());
        System.out.println(httpResponse.body());

    }
}
