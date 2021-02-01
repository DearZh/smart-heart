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
    R listProductCatgories(@PathVariable CloudName type, @PathVariable(required = false) String productCategoryName) throws Exception;

    @ApiOperation(value = "获取问题类别", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "productCategoryId", value = "产品类型ID", required = false, dataType = "String", paramType = "path")})
    @GetMapping(value = {"/listProducts/{productCategoryId}", "/listProducts"})
    R listProducts(@PathVariable CloudName type, @PathVariable(required = false) String productCategoryId) throws Exception;


    @ApiOperation(value = "新建工单", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "email", defaultValue = "zhaozhihao@gangtise.com.cn", value = "接受消息邮箱（阿里必填)", required = false, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "productCategoryId", value = "工单产品类型", required = true, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "withSourceId", defaultValue = "83aeb0f2834c4df49826c781d32a963e", value = "工单来源ID（华为必填）", required = false, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "withBusinessTypeId", value = "工单产品问题类型ID", required = true, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "withSimpleDescription", value = "工单问题", required = true, dataType = "String", paramType = "")})
    @PostMapping(value = "insertOsm")
    R insertOsm(@PathVariable CloudName type, String email, String productCategoryId, String withSourceId, String withSimpleDescription, String withBusinessTypeId) throws Exception;

    //************************************************

    @ApiOperation(value = "获取工单状态", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path")})
    @GetMapping(value = {"/listCaseStatus"})
    R listCaseStatus(@PathVariable CloudName type) throws Exception;

    @ApiOperation(value = "查询工单列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "", required = false, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "page", value = "", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "startTime", value = "", required = false, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "endTime", value = "", required = false, dataType = "String", paramType = "")
    })
    @GetMapping(value = {"/listCase/{page}"})
    R listCase(@PathVariable CloudName type, String status, @PathVariable Integer page, String startTime, String endTime) throws Exception;


    @ApiOperation(value = "新建工单回复", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "caseId", value = "", required = true, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "message", value = "", required = true, dataType = "String", paramType = ""),
            @ApiImplicitParam(name = "messageType", value = "", required = true, dataType = "Long", paramType = ""),
    })
    @PostMapping(value = {"/insertCaseMessage"})
    R insertCaseMessage(@PathVariable CloudName type, String caseId, String message, Integer messageType) throws Exception;

    @ApiOperation(value = "获取工单操作类型|华为", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path")})
    @GetMapping(value = {"/huaweiCaseActionType"})
    R huaweiCaseActionType(@PathVariable CloudName type) throws Exception;

    @ApiOperation(value = "查询工单未读消息的数量|华为", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "caseId", value = "", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = {"/listUnread/{caseId}"})
    R listUnread(@PathVariable CloudName type, @PathVariable String caseId) throws Exception;

    @ApiOperation(value = "设置工单消息为已读 | 华为", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "caseId", value = "", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = {"/caseUnread/{caseId}"})
    R caseUnread(@PathVariable CloudName type, @PathVariable String caseId) throws Exception;


    @ApiOperation(value = "工单操作 | 关闭工单", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "caseId", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "caseActionType", value = "华为工单操作类型", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping(value = {"/caseAction/{caseId}/{caseActionType}"})
    R caseAction(@PathVariable CloudName type, @PathVariable String caseId, @PathVariable String caseActionType) throws Exception;

    @ApiOperation(value = "查询工单详情|华为", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "caseId", value = "", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = {"/showCaseDetail/{caseId}"})
    R showCaseDetail(@PathVariable CloudName type, @PathVariable String caseId) throws Exception;


    @ApiOperation(value = "查询工单留言", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "caseId", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping(value = {"/listMessages/{caseId}/{page}"})
    R listMessages(@PathVariable CloudName type, @PathVariable String caseId, @PathVariable Integer page) throws Exception;


}
