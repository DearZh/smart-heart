package com.gangtise.cloud.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
@Getter
@Setter
@AllArgsConstructor
public class CloudUser {
    private String regionId;
    private String ak;
    private String sk;
}
