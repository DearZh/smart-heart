package com.gangtise.cloud.launcher.mp.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gangtise.cloud.common.constant.BusinessConstant;
import com.gangtise.cloud.common.constant.CloudName;
import com.gangtise.cloud.launcher.mp.entity.CloudUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Description: 
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
@Service
public class InitService {

    @Autowired
    private CloudUserService cloudUserService;

    @PostConstruct
    public void init() {
        List<CloudUser> list = cloudUserService.list(Wrappers.<CloudUser>lambdaQuery().eq(CloudUser::getType, CloudName.ALIBABA).or().eq(CloudUser::getType, CloudName.HUAWEI));

        list.forEach(v -> {
            if (v.getType().equals(CloudName.ALIBABA)) {
                BusinessConstant.cloudUserMap.put(CloudName.ALIBABA, new com.gangtise.cloud.common.entity.CloudUser(v.getRegionId(), v.getSecretId(), v.getSecretKeySecret()));
            } else if (v.getType().equals(CloudName.HUAWEI)) {
                BusinessConstant.cloudUserMap.put(CloudName.HUAWEI, new com.gangtise.cloud.common.entity.CloudUser(v.getRegionId(), v.getSecretId(), v.getSecretKeySecret()));
            }
        });

/*
        CloudUser huawei = new CloudUser("", "MQA4PRORG7QMYULUPFBH", "qPqUDMIjBjuhtP5geWyV0M70cnEguVbjIKvU0ekI");
        com.gangtise.cloud.common.entity.CloudUser alibaba = new CloudUser("cn-hangzhou", "LTAI4GKyF9bKSG75ZVkRKF19", "TciNHshPDtRi8jclhjCzK7mwThfZ04");
        BusinessConstant.cloudUserMap.put(CloudName.HUAWEI, huawei);
        BusinessConstant.cloudUserMap.put(CloudName.ALIBABA, alibaba);*/
    }
}
