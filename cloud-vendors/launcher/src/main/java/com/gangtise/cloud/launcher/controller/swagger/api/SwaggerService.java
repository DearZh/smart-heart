package com.gangtise.cloud.launcher.controller.swagger.api;

import com.gangtise.cloud.launcher.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/9
 */
@Api(tags = "Swagger API TEST")
public interface SwaggerService {

    // swagger 注解参考： https://www.cnblogs.com/kingsonfu/p/11519728.html

    @ApiOperation(value = "从路径中发送参数", notes = "")
    @ApiImplicitParam(name = "pathName", value = "", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "testPath/{pathName}")
    public R<String> testPath(@PathVariable String pathName);

    @ApiOperation(value = "从Params中发送参数", notes = "")
    @ApiImplicitParam(name = "paramName", value = "", required = true, dataType = "String", paramType = "query")
    @GetMapping(value = "testParam")
    public R<String> testParam(String paramName);


    @ApiOperation(value = "从路径以及参数中发送参数", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "pathName", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "paramName", value = "", required = true, dataType = "String", paramType = "query")})
    @GetMapping(value = "testPathParam/{pathName}")
    public R<String> testPathParam(@PathVariable(value = "pathName") String pathName, @RequestParam(value = "paramName") String paramName);


    @ApiOperation(value = " header中发送参数", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "Author_Token", value = "", required = true, dataType = "String", paramType = "header")})
    @GetMapping(value = "/testHeader")
    public R<String> testHeader(@RequestHeader(value = "Author_Token") String authorToken);

    //一个接口多种传参方式，pathNameTwo非必传
    @ApiOperation(value = " 一个接口多种传参方式", notes = "一个接口多种传参方式，pathNameTwo非必传")
    @ApiImplicitParams({@ApiImplicitParam(name = "pathName", value = "", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "pathNameTwo", value = "", required = false, dataType = "String", paramType = "path")})
    @GetMapping(value = {"testPathMany/{pathName}/{pathNameTwo}", "testPathMany/{pathName}"})
    public R<String> testPathMany(@PathVariable String pathName, @PathVariable(required = false) String pathNameTwo);


    //路径传参时，涉及到内容包含小数点时，必须注解中增加 :.+ 的标识，否则小数点后的数据将会提交时自动截断
    @ApiOperation(value = " 路径传参小数点问题", notes = "路径传参时，涉及到内容包含小数点时，必须注解中增加 :.+ 的标识，否则小数点后的数据将会提交时自动截断")
    @ApiImplicitParams({@ApiImplicitParam(name = "pathName", value = "", required = true, dataType = "String", paramType = "path")})
    @GetMapping(value = {"testPathDecimal/{pathName:.+}"})
    public R<String> testPathDecimal(@PathVariable String pathName);


    //使用API Model 指定了传参实体类的说明后，则无需再使用ApiImplicitParams进行参数说明了

    @ApiOperation(value = " ApiModel的用法", notes = "ApiModel 表示对类进行说明，用于实体类中的参数接收说明")
    @PostMapping(value = "getParamsR")
    public R<String> getParamsR(R<String> r);

}
