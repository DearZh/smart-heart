package com.gangtise.cloud.vendors.alibaba.osm;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.workorder.model.v20200326.ListProductsRequest;
import com.aliyuncs.workorder.model.v20200326.ListProductsResponse;
import com.google.gson.Gson;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
public class Test {

    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "", "");
        IAcsClient client = new DefaultAcsClient(profile);

        ListProductsRequest request = new ListProductsRequest();
//        request.setRegionId("cn-hangzhou");

        try {
            ListProductsResponse response = client.getAcsResponse(request);
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
