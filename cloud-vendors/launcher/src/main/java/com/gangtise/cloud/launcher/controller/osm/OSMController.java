package com.gangtise.cloud.launcher.controller.osm;


import com.gangtise.cloud.common.constant.CloudName;
import com.gangtise.cloud.common.osm.service.OSMService;
import com.gangtise.cloud.launcher.controller.osm.api.OSMSwaggerService;
import com.gangtise.cloud.launcher.util.CloudBuild;
import com.gangtise.cloud.launcher.util.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
@RestController
@RequestMapping("/osm/{type}")
public class OSMController implements OSMSwaggerService {

    /**
     * 获取产品类别
     *
     * @return
     */
    @Override
    public R listProductCatgories(CloudName type, String productCategoryName) throws Exception {
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.listProductCatgories(productCategoryName);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();
    }

    /**
     * 获取问题类别
     *
     * @param type
     * @param productCategoryId
     * @return
     * @throws Exception
     */
    @Override
    public R listProducts(CloudName type, String productCategoryId) throws Exception {
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.listProducts(productCategoryId);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();
    }

    @Override
    public R insertOsm(CloudName type, String productCategoryId, String withSourceId, String withSimpleDescription, String withBusinessTypeId) throws Exception {
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.insertOsm(productCategoryId, withSourceId, withSimpleDescription, withBusinessTypeId);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();
    }


}
