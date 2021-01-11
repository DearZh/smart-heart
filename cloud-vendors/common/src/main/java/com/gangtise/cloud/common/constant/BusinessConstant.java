package com.gangtise.cloud.common.constant;

import com.gangtise.cloud.common.entity.CloudUser;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
public class BusinessConstant {
    public static ConcurrentHashMap<CloudName, CloudUser> cloudUserMap = new ConcurrentHashMap<CloudName, CloudUser>();


}
