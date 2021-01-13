package com.smart.heart.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.heart.mybatisplus.entity.User;
import com.smart.heart.mybatisplus.service.UserServer;
import org.junit.jupiter.api.Test;
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
    void pageQuery() {
        Page<User> page = new Page<>();
        //默认自动获取count值，如果不需要自动获取 设置hitCount(true) 则不自动获取count数
//        page.hitCount(true); page.setTotal(userServer.count());
        page.setSize(3);
        page.setCurrent(1);
        userServer.page(page);

        System.out.println(page);
        System.out.println(page.getTotal());
        //关于xml mapper分页 reference： https://www.cnblogs.com/yscec/p/12564113.html
    }


    @Test
    void updateWrapper() {
        userServer.update(Wrappers.<User>update().set("age", 12).set("name", "23").eq("", ""));
        userServer.update(Wrappers.<User>update().lambda().set(User::getAge, "23").eq(User::getName, "na"));
    }

    @Test
    void selectGet() {
        User user = userServer.getById(2);
        System.out.println(user);


        Map<String, Object> map = userServer.getMap(new QueryWrapper<User>().lambda().eq(User::getAge, 12));
//        Wrappers.<User>query().eq("","");
//        Wrappers.<User>lambdaQuery().eq(User::getAge, 12)
//        new QueryWrapper<User>().select("id","name","age").eq()

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
