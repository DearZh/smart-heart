package com.gangtise.cloud.launcher.controller.osm.swagger;

import com.gangtise.cloud.common.constant.CloudName;
import com.gangtise.cloud.launcher.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
@Api(tags = "OSM工单")
public interface OSMSwaggerService {


    @ApiOperation(value = "查询华为产品类型列表", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "productCategoryName", value = "产品类型名称", required = false, dataType = "String", paramType = "path")})
    @GetMapping(value = {"/listProductCatgories/{productCategoryName}", "/listProductCatgories"})
    R<String> listProductCatgories(@PathVariable CloudName type, @PathVariable(required = false) String productCategoryName) throws Exception;

    @ApiOperation(value = "获取问题类别", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "productCategoryId", value = "产品类型ID", required = false, dataType = "String", paramType = "path")})
    @GetMapping(value = {"/listProducts/{productCategoryId}", "/listProducts"})
    R<String> listProducts(@PathVariable CloudName type, @PathVariable(required = false) String productCategoryId) throws Exception;
}
