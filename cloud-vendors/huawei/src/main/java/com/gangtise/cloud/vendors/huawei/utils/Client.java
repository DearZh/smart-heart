package com.gangtise.cloud.vendors.huawei.utils;

import com.gangtise.cloud.common.constant.BusinessConstant;
import com.gangtise.cloud.common.constant.CloudName;
import com.gangtise.cloud.common.entity.CloudUser;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.osm.v2.OsmClient;
import com.huaweicloud.sdk.osm.v2.region.OsmRegion;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
public class Client {
    public static OsmClient create() {
        CloudUser cloudUser = BusinessConstant.cloudUserMap.get(CloudName.HUAWEI);
        String ak = cloudUser.getAk();
        String sk = cloudUser.getSk();

        ICredential auth = new GlobalCredentials()
                .withAk(ak)
                .withSk(sk);

        OsmClient client = OsmClient.newBuilder()
                .withCredential(auth)
                .withRegion(OsmRegion.CN_SOUTH_1)
                .build();
        return client;
    }
}
