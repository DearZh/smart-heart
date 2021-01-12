package com.gangtise.cloud.launcher.controller.osm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gangtise.cloud.launcher.controller.osm.api.CloudUserSwaggerService;
import com.gangtise.cloud.launcher.mp.entity.CloudUser;
import com.gangtise.cloud.launcher.mp.service.CloudUserService;
import com.gangtise.cloud.launcher.util.ParameterCheck;
import com.gangtise.cloud.launcher.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/12
 */
@RestController
@RequestMapping("/cloud/user")
public class CloudUserController implements CloudUserSwaggerService {

    @Autowired
    private CloudUserService cloudUserService;

    @Override
    public R insert(CloudUser cloudUser) throws Exception {
        ParameterCheck.of(cloudUser).isNull(v -> StringUtils.isNotBlank(v.getSecretId()) && StringUtils.isNotBlank(v.getSecretKeySecret()));
        cloudUser.setCreateTime(System.currentTimeMillis());
        boolean flag = cloudUserService.save(cloudUser);
        if (flag)
            return R.ok();
        return R.failed();
    }

    @Override
    public R update(CloudUser cloudUser) throws Exception {
        ParameterCheck.of(cloudUser).isNull(v -> StringUtils.isNotBlank(v.getSecretId()) && StringUtils.isNotBlank(v.getSecretKeySecret()) && StringUtils.isNotBlank(v.getId()));
        cloudUser.setCreateTime(System.currentTimeMillis());
        boolean flag = cloudUserService.saveOrUpdate(cloudUser);
        if (flag)
            return R.ok();
        return R.failed();
    }

    @Override
    public R page(Integer current) throws Exception {
        Page<CloudUser> rPage = new Page<>();
        rPage.setCurrent(current);
        return R.ok(cloudUserService.page(rPage));
    }


}
