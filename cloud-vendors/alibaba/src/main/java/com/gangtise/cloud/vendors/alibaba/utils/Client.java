package com.gangtise.cloud.vendors.alibaba.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.gangtise.cloud.common.constant.BusinessConstant;
import com.gangtise.cloud.common.constant.CloudName;
import com.gangtise.cloud.common.entity.CloudUser;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
public class Client {


    public static IAcsClient client() {
        CloudUser cloudUser = BusinessConstant.cloudUserMap.get(CloudName.ALIBABA);

        DefaultProfile profile = DefaultProfile.getProfile(cloudUser.getRegionId(),
                cloudUser.getAk(), cloudUser.getSk());
        IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
