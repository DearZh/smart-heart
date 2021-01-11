package com.gangtise.cloud.launcher.service;

import com.gangtise.cloud.common.constant.BusinessConstant;
import com.gangtise.cloud.common.constant.CloudName;
import com.gangtise.cloud.common.entity.CloudUser;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
@Service
public class InitService {

    @PostConstruct
    public void init() {
        CloudUser huawei = new CloudUser("", "MQA4PRORG7QMYULUPFBH", "qPqUDMIjBjuhtP5geWyV0M70cnEguVbjIKvU0ekI");

        CloudUser alibaba = new CloudUser("cn-hangzhou", "LTAI4GKyF9bKSG75ZVkRKF19", "TciNHshPDtRi8jclhjCzK7mwThfZ04");

        BusinessConstant.cloudUserMap.put(CloudName.HUAWEI, huawei);
        BusinessConstant.cloudUserMap.put(CloudName.ALIBABA, alibaba);
    }
}
