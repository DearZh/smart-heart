package com.gangtise.cloud.vendors.huawei.constant;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/12/28
 */
public class HuaWeiConstant {

    public static final String HUAWEI_SHANGHAI_TOKEN_ENDPOINT = "https://iam.cn-south-1.myhuaweicloud.com";
    public static final String HUAWEI_TOKEN_URL = HUAWEI_SHANGHAI_TOKEN_ENDPOINT + "/v3/auth/tokens";
    //IAM用户所属账号信息（华为云企业账号)
    public static final String HUAWEI_USER_ACCOUNT = "hw29187581";

    //IAM 账号
    public static final String HUAWEI_IAM_USER_ACCOUNT = "zhaozhihao";
    public static final String HUAWEI_IAM_PASSWORD = "4Hqm44bE83";
    //华为云返回Token时的 header key
    public static final String HUAWEI_HEADER_TOKEN = "X-Subject-Token";
    //token 的刷新时间，默认23小时刷新一次
    public static final Long HUAWEI_REFER_TIME_TOKEN = 82800000L;

    //华南广州OSM 工单终端URL
    public static final String HUAWEI_CASE_URL = "https://osm.cn-south-1.myhuaweicloud.com";
    //华为云工单列表
    public static final String HUAWEI_CASE_LIST = "/v1.0/servicerequest/case";
    //请求华为云接口时header key
    public static final String HUAWEI_REQUEST_TOKEN = "X-Auth-Token";
}
