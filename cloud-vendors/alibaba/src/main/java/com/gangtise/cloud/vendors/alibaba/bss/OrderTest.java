package com.gangtise.cloud.vendors.alibaba.bss;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.bssopenapi.model.v20171214.QueryOrdersRequest;
import com.aliyuncs.bssopenapi.model.v20171214.QueryOrdersResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
public class OrderTest {
    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "", "");
        IAcsClient client = new DefaultAcsClient(profile);

        QueryOrdersRequest request = new QueryOrdersRequest();
        request.setRegionId("cn-hangzhou");

        try {
            QueryOrdersResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
}
