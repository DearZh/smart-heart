package com.smart.heart.hutool.huawei.token;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 构建华为云Token参数
 * @Author: Arnold.zhao
 * @Date: 2020/12/28
 */
public class BuildAuth {
    private final String METHODS = "password";
    private AuthObj authObj = new AuthObj();


    /**
     * build  Identity
     *
     * @param userAccount    IAM 用户所属的账号名
     * @param iamUserAccount IAM账户
     * @param iamPassword    IAM密码
     * @return
     */
    public BuildAuth buildIdentity(String userAccount, String iamUserAccount, String iamPassword) {
        AuthObj.Identity identity = authObj.new Identity();
        identity.setMethods(Arrays.asList(METHODS));
        AuthObj.Identity.PassWord passWord = identity.new PassWord();
        AuthObj.Identity.PassWord.User user = passWord.new User(iamUserAccount, iamPassword, null);
        passWord.setUser(user);
        user.setDomain(user.new Domain(userAccount));
        identity.setPassword(passWord);

        authObj.setIdentity(identity);
        return this;
    }


    public BuildAuth buildScope() {
        AuthObj.Scope scope = authObj.new Scope();

        return this;
    }

    public static BuildAuth create() {
        return new BuildAuth();
    }

    public String toJSONString() {
        Map<String, AuthObj> map = new HashMap<>();
        map.put("auth", authObj);
        return JSONObject.toJSONString(map);
    }


    @Data
    class AuthObj {
        private Identity identity;
        private Scope scope;

        @Data
        class Identity {
            //认证方式（password）
            private List<String> methods;
            private Identity.PassWord password;

            @Data
            class PassWord {
                private Identity.PassWord.User user;

                @Data
                @AllArgsConstructor
                class User {
                    private String name;//iam 用户名
                    private String password;//IAM 的用户登录密码
                    private Identity.PassWord.User.Domain domain;  //IAM 用户所属的账号名

                    @Data
                    @AllArgsConstructor
                    class Domain {
                        String name;//IAM用户所属的账号名
                    }
                }
            }
        }

        @Data
        class Scope {
            private Scope.Domain domain;
            private Scope.Project project;

            @Data
            class Domain {
                private String id;//IAM 用户所属账号ID；
                private String name;//IAM 用户所属账号名称
            }

            @Data
            class Project {
                private String id;//IAM用户所属账号的项目ID
                private String name;//IAM用户所属账号的项目名称
            }
        }
    }
}
