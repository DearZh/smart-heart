package com.smart.heart.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.smart.heart.mybatisplus.entity.User;
import com.smart.heart.mybatisplus.mapper.UserMapper;
import com.smart.heart.mybatisplus.service.UserServer;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MybatisplusApplicationTests {

    @Autowired
    private UserServer userServer;

    @Test


    void selectGet() {
        User user = userServer.getById(2);
        System.out.println(user);

        Map<String, Object> map = userServer.getMap(new QueryWrapper<User>().lambda().eq(User::getAge, 12));
        //condition表示当前该条件是否加入最后生成的SQL中
        Map<String, Object> map1 = userServer.getMap(new QueryWrapper<User>().lambda().eq(user.getAge() != null, User::getAge, 12));

        System.out.println(map.get("age"));
        System.out.println(map.get("id"));
        System.out.println("*************");
    }

    @Test
    void remove() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("name", "update-test1");
        userServer.removeByMap(map);
//        userServer.removeById()
//        userServer.removeByIds()
//        userServer.remove(Wrapper)
    }

    @Test
    void updateBatch() {
        User user = new User();
        user.setAge(12);
        user.setName("update-test1");
        user.setRoleId(1L);
        user.setId(1L);
        userServer.updateBatchById(Arrays.asList(user));
    }

    @Test
    void saveBatch() {
        User user = new User();
        user.setAge(12);
        user.setName("batch-test1");
        user.setRoleId(1L);
        User user1 = new User();
        user1.setAge(12);
        user1.setName("batch-test2");
        user1.setRoleId(1L);
        userServer.saveBatch(Arrays.asList(user, user1));
    }

    @Test
    void save() {
        User user = new User();
        user.setAge(12);
        user.setName("test1");
        user.setRoleId(1L);
        boolean flag = userServer.save(user);
        System.out.println("insert flag=" + flag);
    }

}
