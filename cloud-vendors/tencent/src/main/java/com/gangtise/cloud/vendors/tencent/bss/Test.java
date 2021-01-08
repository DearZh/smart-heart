package com.gangtise.cloud.vendors.tencent.bss;

import com.tencentcloudapi.billing.v20180709.BillingClient;
import com.tencentcloudapi.billing.v20180709.models.DescribeAccountBalanceRequest;
import com.tencentcloudapi.billing.v20180709.models.DescribeAccountBalanceResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/7
 */
public class Test {

    public static void main(String[] args) {
        try {

            //secretId , secretKey
            Credential cred = new Credential("AKIDlIiqhGCq1zKRHr6gk9O2QXO5VeWCZywd", "rSu67sMZYRTFkzPdZL4nHhqlPmZ3GCw2");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("billing.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            BillingClient client = new BillingClient(cred, "", clientProfile);

            DescribeAccountBalanceRequest req = new DescribeAccountBalanceRequest();


            DescribeAccountBalanceResponse resp = client.DescribeAccountBalance(req);

            System.out.println(DescribeAccountBalanceResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

    }
}
