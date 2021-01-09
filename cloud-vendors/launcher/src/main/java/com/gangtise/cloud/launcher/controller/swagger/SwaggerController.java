package com.gangtise.cloud.launcher.controller.swagger;

import com.gangtise.cloud.launcher.controller.swagger.swagger.SwaggerService;
import com.gangtise.cloud.launcher.util.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 接口分离，与参数绑定相关的注解(包含swagger文档注解）全部移动至接口中定义，避免当前Controller注解较多及其难看混乱
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
@RestController
@RequestMapping("/test")
public class SwaggerController implements SwaggerService {

    @Override
    public R<String> testPath(String pathName) {
        return R.ok(pathName);
    }

    @Override
    public R<String> testParam(String paramName) {
        return R.ok(paramName);
    }

    @Override
    public R<String> testPathParam(String pathName, String paramName) {
        return R.ok(pathName + paramName);
    }

    @Override
    public R<String> testHeader(String authorToken) {
        return R.ok(authorToken, "header中参数获取成功");
    }


    @Override
    public R<String> testPathMany(String pathName, String pathNameTwo) {
        return R.ok(pathName);
    }

    @Override
    public R<String> testPathDecimal(String pathName) {
        return R.ok(pathName, "包含小数点的数据获取");
    }

    @Override
    public R<String> getParamsR(R<String> r) {
        return r;
    }

}
