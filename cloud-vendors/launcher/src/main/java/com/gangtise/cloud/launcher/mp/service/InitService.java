package com.gangtise.cloud.launcher.mp.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gangtise.cloud.common.constant.BusinessConstant;
import com.gangtise.cloud.common.constant.CloudName;
import com.gangtise.cloud.launcher.mp.entity.CloudUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
@Service
public class InitService {

    @Autowired
    private CloudUserService cloudUserService;

    @PostConstruct
    public void init() {
        initCaseCode();
        initCloudUser();
    }

    private void initCloudUser() {
        List<CloudUser> list = cloudUserService.list(Wrappers.<CloudUser>lambdaQuery().eq(CloudUser::getType, CloudName.ALIBABA).or().eq(CloudUser::getType, CloudName.HUAWEI));
        list.forEach(v -> {
            if (v.getType().equals(CloudName.ALIBABA)) {
                BusinessConstant.cloudUserMap.put(CloudName.ALIBABA, new com.gangtise.cloud.common.entity.CloudUser(v.getRegionId(), v.getSecretId(), v.getSecretKeySecret()));
            } else if (v.getType().equals(CloudName.HUAWEI)) {
                BusinessConstant.cloudUserMap.put(CloudName.HUAWEI, new com.gangtise.cloud.common.entity.CloudUser(v.getRegionId(), v.getSecretId(), v.getSecretKeySecret()));
            }
        });
    }

    private void initCaseCode() {
        BusinessConstant.huaweiCaseCode.put("0", "待受理");
        BusinessConstant.huaweiCaseCode.put("1", "处理中");
        BusinessConstant.huaweiCaseCode.put("2", "待确认结果");
        BusinessConstant.huaweiCaseCode.put("3", "已完成");
        BusinessConstant.huaweiCaseCode.put("4", "已撤销");
        BusinessConstant.huaweiCaseCode.put("12", "无效");
        BusinessConstant.huaweiCaseCode.put("17", "待反馈");
        //ali
        BusinessConstant.alibabaCaseCode.put("in_progress", "处理中");
        BusinessConstant.alibabaCaseCode.put("wait_feedback", "待您反馈");
        BusinessConstant.alibabaCaseCode.put("wait_confirm", "待您确认");
        BusinessConstant.alibabaCaseCode.put("completed", "已关闭");
    }
}
