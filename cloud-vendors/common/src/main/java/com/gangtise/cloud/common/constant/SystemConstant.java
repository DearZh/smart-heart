package com.gangtise.cloud.common.constant;

/**
 * @Description: 可继承 GangtiseCodeConstants
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
public class SystemConstant {

    /**
     * 操作成功！
     */
    public static final String CODE_000000 = "000000";

    /**
     * 未知异常，请联系管理员！
     */
    public static final String CODE_999999 = "999999";

    /**
     * 分页查询时默认查询的数据条数
     */
    public static final Long size = 20L;
    public static final String HTTP_200 = "200";
    public static final String OSM_CLOSE = "close";

    public class HuaWei {
        public static final String HUAWEI_REQUEST_TOKEN = "X-Auth-Token";
    }

    public class Tencent {
        public static final String HUAWEI_REQUEST_TOKEN = "X-Auth-Token";
    }

    public class Alibaba {
        public static final String HUAWEI_REQUEST_TOKEN = "X-Auth-Token";
    }
}
