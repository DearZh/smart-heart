package com.gangtise.cloud.launcher.controller.osm.api;

import com.gangtise.cloud.launcher.mp.entity.CloudUser;
import com.gangtise.cloud.launcher.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/12
 */
@Api(tags = "云厂商账号管理")
public interface CloudUserSwaggerService {

    @ApiOperation(value = "新建厂商信息", notes = "")
//    @ApiImplicitParam(name = "cloudUser", value = "厂商账号实体", paramType = "body", required = true, dataType = "CloudUser")
    @PostMapping(value = {"/insert"})
    R insert(@RequestBody CloudUser cloudUser) throws Exception;

    @ApiOperation(value = "更新厂商信息", notes = "")
    @PutMapping(value = {"/update"})
    R update(@RequestBody CloudUser cloudUser) throws Exception;

    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = {"/page/{current}"})
    R page(@PathVariable Integer current) throws Exception;


}
