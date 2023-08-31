package com.gangtise.cloud.launcher.util;

import com.gangtise.cloud.common.constant.SystemConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: 响应消息主体，参照mybatis-plus R类 <br/> 与gangtise framework结合继承ResultForm类重写方法体即可
 * @Author: Arnold.zhao
 * @Date: 2021/1/9
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(description = "响应信息主体")
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;


    @Getter
    @Setter
    @ApiModelProperty(value = "返回标记：成功=000000，失败=999999")
    private String code;

    @Getter
    @Setter
    @ApiModelProperty(value = "返回信息")
    private String msg;

    @Getter
    @Setter
    @ApiModelProperty(value = "接口调用是否正常")
    private Boolean status;


    @Getter
    @Setter
    @ApiModelProperty(value = "数据")
    private T data;


    public static <T> R<T> ok() {
        return restResult(null, SystemConstant.CODE_000000, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SystemConstant.CODE_000000, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, SystemConstant.CODE_000000, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, SystemConstant.CODE_999999, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, SystemConstant.CODE_999999, msg);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, SystemConstant.CODE_999999, null);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, SystemConstant.CODE_999999, msg);
    }

    private static <T> R<T> restResult(T data, String code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        if (code.equals(SystemConstant.CODE_000000)) {
            apiResult.setStatus(true);
        } else {
            apiResult.setStatus(false);
        }
        return apiResult;
    }
}