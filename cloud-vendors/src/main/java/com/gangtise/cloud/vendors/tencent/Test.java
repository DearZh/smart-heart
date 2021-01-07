package com.gangtise.cloud.vendors.tencent;
import com.tencentcloudapi.billing.v20180709.BillingClient;
import com.tencentcloudapi.billing.v20180709.models.*;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/7
 */
public class Test {

    public static void main(String [] args) {
        try{


            Credential cred = new Credential("AKIDrVyFNQ4CJA3h4JFWTDuMtAftPsZBmFjy", "K9gynVmx0ThgPtEUWRQI4buKf8XYKJ1S");

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
