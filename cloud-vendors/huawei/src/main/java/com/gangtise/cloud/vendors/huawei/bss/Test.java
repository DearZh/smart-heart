package com.gangtise.cloud.vendors.huawei.bss;

import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.bss.v2.*;
import com.huaweicloud.sdk.bss.v2.region.BssRegion;
import com.huaweicloud.sdk.bss.v2.model.*;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
public class Test {
    public static void main(String[] args) {
        String ak = "MQA4PRORG7QMYULUPFBH";
        String sk = "qPqUDMIjBjuhtP5geWyV0M70cnEguVbjIKvU0ekI";

        ICredential auth = new GlobalCredentials()
                .withAk(ak)
                .withSk(sk);

        BssClient client = BssClient.newBuilder()
                .withCredential(auth)
                .withRegion(BssRegion.CN_NORTH_1)
                .build();
        ListSubCustomerCouponsRequest request = new ListSubCustomerCouponsRequest();
        try {
            ListSubCustomerCouponsResponse response = client.listSubCustomerCoupons(request);
            System.out.println(response.toString());
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (RequestTimeoutException e) {
            e.printStackTrace();
        } catch (ServiceResponseException e) {
            e.printStackTrace();
            System.out.println(e.getHttpStatusCode());
            System.out.println(e.getErrorCode());
            System.out.println(e.getErrorMsg());
        }
    }
}
