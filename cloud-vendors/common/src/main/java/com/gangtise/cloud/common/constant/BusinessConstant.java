package com.gangtise.cloud.common.constant;

import com.gangtise.cloud.common.entity.CloudUser;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
public class BusinessConstant {
    /**
     * 云厂商账号信息
     */
    public static ConcurrentHashMap<CloudName, CloudUser> cloudUserMap = new ConcurrentHashMap<CloudName, CloudUser>();
    /**
     * 华为工单状态
     */
    public static ConcurrentHashMap<String, String> huaweiCaseCode = new ConcurrentHashMap<String, String>();
    /**
     * 阿里工单状态
     */
    public static ConcurrentHashMap<String, String> alibabaCaseCode = new ConcurrentHashMap<String, String>();

    /**
     * 华为工单操作的几种类型
     */
    public static ConcurrentHashMap<String, String> huaweiCaseActionType = new ConcurrentHashMap<String, String>();
}
