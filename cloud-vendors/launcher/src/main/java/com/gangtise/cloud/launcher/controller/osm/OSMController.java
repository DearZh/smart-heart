package com.gangtise.cloud.launcher.controller.osm;


import com.gangtise.cloud.common.osm.service.OSMService;
import com.gangtise.cloud.launcher.util.CloudBuild;
import com.gangtise.cloud.launcher.util.R;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
@RestController
@RequestMapping("/osm/{type}")
public class OSMController {


    /**
     * 获取产品类别
     *
     * @return
     */
    public R<String> listProductCatgories(@PathVariable String type) {
        OSMService osmService = CloudBuild.OSM().create(type);

        return null;
    }


}
