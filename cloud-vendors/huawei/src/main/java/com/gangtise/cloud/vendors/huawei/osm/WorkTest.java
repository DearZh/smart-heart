package com.gangtise.cloud.vendors.huawei.osm;

import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.osm.v2.*;
import com.huaweicloud.sdk.osm.v2.region.OsmRegion;
import com.huaweicloud.sdk.osm.v2.model.*;

/**
 * @Description: 华为工单SDK
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
public class WorkTest {

    public static void main(String[] args) {
        String ak = "MQA4PRORG7QMYULUPFBH";
        String sk = "qPqUDMIjBjuhtP5geWyV0M70cnEguVbjIKvU0ekI";

        ICredential auth = new GlobalCredentials()
                .withAk(ak)
                .withSk(sk);

        OsmClient client = OsmClient.newBuilder()
                .withCredential(auth)
                .withRegion(OsmRegion.CN_SOUTH_1)
                .build();
        ListCasesRequest request = new ListCasesRequest();
        try {
            ListCasesResponse response = client.listCases(request);
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
