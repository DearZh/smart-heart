package com.gangtise.cloud.launcher.controller.osm;

import com.gangtise.cloud.common.constant.SystemConstant;
import com.gangtise.cloud.launcher.controller.osm.swagger.SwaggerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
@RestController(value = "/test")
public class SwaggerController implements SwaggerService {

    @GetMapping(value = "test")
    public String test(String name) {
        return name;
    }

    @GetMapping(value = "constant")
    public String constant(String name) {
        return SystemConstant.HuaWei.HUAWEI_REQUEST_TOKEN;
    }
}
