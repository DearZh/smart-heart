package com.gangtise.cloud.launcher.controller.osm.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
@Api(tags = "Controller Test")
public interface SwaggerService {

    @ApiOperation(value = "API Get验证", notes = "用于做接口测试使用，传入一个名称，返回结果和原名称相同")
    @ApiImplicitParam(name = "name", value = "", required = true, dataType = "String")
    public String test(String name);
}
