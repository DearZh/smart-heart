package com.gangtise.cloud.launcher.controller.osm.api;

import com.gangtise.cloud.common.constant.CloudName;
import com.gangtise.cloud.launcher.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
@Api(tags = "OSM工单")
public interface OSMSwaggerService {


    @ApiOperation(value = "查询产品类型列表", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "productCategoryName", value = "产品类型名称", required = false, dataType = "String", paramType = "path")})
    @GetMapping(value = {"/listProductCatgories/{productCategoryName}", "/listProductCatgories"})
    R<String> listProductCatgories(@PathVariable CloudName type, @PathVariable(required = false) String productCategoryName) throws Exception;

    @ApiOperation(value = "获取问题类别", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "productCategoryId", value = "产品类型ID", required = false, dataType = "String", paramType = "path")})
    @GetMapping(value = {"/listProducts/{productCategoryId}", "/listProducts"})
    R<String> listProducts(@PathVariable CloudName type, @PathVariable(required = false) String productCategoryId) throws Exception;


    @ApiOperation(value = "新建工单", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "email", value = "接受消息邮箱（阿里必填)", required = false, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "productCategoryId", value = "工单产品类型", required = true, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "withSourceId", value = "工单来源ID（华为必填）", required = false, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "withBusinessTypeId", value = "工单产品问题类型ID", required = true, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "withSimpleDescription", value = "工单问题", required = true, dataType = "String", paramType = "")})
    @PostMapping(value = "insertOsm")
    R insertOsm(@PathVariable CloudName type, String email, String productCategoryId, String withSourceId, String withSimpleDescription, String withBusinessTypeId) throws Exception;
}
