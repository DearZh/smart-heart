package com.gangtise.cloud.launcher.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CloudOsmProductType {



    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String type;
    private String name;

    private Long createTime;

}
