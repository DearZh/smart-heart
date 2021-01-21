package com.gangtise.cloud.launcher;

import com.gangtise.cloud.launcher.mp.entity.CloudOsmList;
import com.gangtise.cloud.launcher.mp.service.CloudOsmDetailService;
import com.gangtise.cloud.launcher.mp.service.CloudOsmListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LauncherApplicationTests {

    @Autowired
    private CloudOsmListService cloudOsmListService;

    @Autowired
    private CloudOsmDetailService cloudOsmDetailService;


    @Test
    void contextLoads() {
        CloudOsmList cloudOsmList = new CloudOsmList();
        cloudOsmList.setCloudId("A");
        cloudOsmList.setCreateTime(System.currentTimeMillis());
        cloudOsmListService.save(cloudOsmList);
    }

}
