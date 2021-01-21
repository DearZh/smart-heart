package com.gangtise.cloud.launcher.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/21
 */
@Getter
@Setter
@TableName(value = "cloud_osm_detail")
public class CloudOsmDetail {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String type;
    private String name;
    private String description;
    private String acronym;
    private Long createTime;
}
