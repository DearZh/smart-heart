package com.gangtise.cloud.launcher.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gangtise.cloud.common.constant.CloudName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 云产商账户信息表
 * @Author: Arnold.zhao
 * @Date: 2021/1/12
 */
@Setter
@Getter
@TableName(value = "cloud_user")
@ApiModel(description = "云产商账号实体")
public class CloudUser {

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "数据编辑及删除时需提供ID")
    private String id;
    @ApiModelProperty(value = "secretId", required = true)
    private String secretId;
    @ApiModelProperty(value = "secretKeySecret", required = true)
    private String secretKeySecret;
    @ApiModelProperty(value = "API地域信息，阿里填写：cn-hangzhou 华为无需填写")
    private String regionId;
    @ApiModelProperty(value = "产商类型")
    private CloudName type;
    @ApiModelProperty(hidden = true)
    private Long createTime;

}
