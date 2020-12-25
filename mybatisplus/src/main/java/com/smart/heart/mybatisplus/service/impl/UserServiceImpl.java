package com.smart.heart.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.heart.mybatisplus.entity.User;
import com.smart.heart.mybatisplus.mapper.UserMapper;
import com.smart.heart.mybatisplus.service.UserServer;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/12/25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserServer {
}
