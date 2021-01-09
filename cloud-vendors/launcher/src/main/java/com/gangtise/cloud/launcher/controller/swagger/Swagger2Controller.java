package com.gangtise.cloud.launcher.controller.swagger;

import com.gangtise.cloud.launcher.controller.swagger.swagger.Swagger2Service;
import com.gangtise.cloud.launcher.util.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/9
 */
@RestController
@RequestMapping(value = "/swagger2/{type}")
public class Swagger2Controller implements Swagger2Service {

    @GetMapping(value = "testPath2/{pathName}")
    public R<String> testPath(@PathVariable String pathName, @PathVariable String type) {
        return R.ok(pathName + type);
    }

}
