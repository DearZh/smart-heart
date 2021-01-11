package com.gangtise.cloud.launcher.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gangtise.cloud.common.constant.CloudName;
import com.gangtise.cloud.common.osm.service.OSMService;
import com.gangtise.cloud.vendors.alibaba.osm.AlibabaOSMService;
import com.gangtise.cloud.vendors.huawei.osm.HuaWeiOSMService;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
public class CloudBuild {

    public static OSMClass OSM() {
        return new CloudBuild().new OSMClass();
    }

    public class OSMClass {
        public OSMService create(String type) {
            if (StringUtils.isNotBlank(type)) {
                if (CloudName.ALIBABA.name().equals(type)) {
                    return new AlibabaOSMService();
                } else if (CloudName.HUAWEI.name().equals(type)) {
                    return new HuaWeiOSMService();
                }
            }
            return null;
        }
    }


}
