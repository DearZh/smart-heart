package com.gangtise.cloud.launcher.util;

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
        public OSMService create(CloudName type) {
            if (type != null) {
                if (CloudName.ALIBABA.equals(type)) {
                    return new AlibabaOSMService();
                } else if (CloudName.HUAWEI.equals(type)) {
                    return new HuaWeiOSMService();
                }
            }
            return null;
        }
    }


}
