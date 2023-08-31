package com.gangtise.cloud.launcher.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/20
 */
@Getter
@Setter
public class CloudOsmProduct {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String type;
    private String name;
    private String description;
    private String acronym;
    private Long createTime;
}
