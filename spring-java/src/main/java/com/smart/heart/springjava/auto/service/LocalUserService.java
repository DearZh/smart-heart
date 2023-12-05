package com.smart.heart.springjava.auto.service;

import com.google.auto.service.AutoService;

/**
 * @author: arnold.zhao
 * @email: zhihao.zhao@ingeek.com
 * @date: 2023/12/5
 */
@AutoService(UserService.class)
public class LocalUserService implements UserService {
    @Override
    public String userName() {
        return "I'm LocalService";
    }
}
