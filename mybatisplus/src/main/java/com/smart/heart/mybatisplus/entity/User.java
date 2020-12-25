package com.smart.heart.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/12/25
 */
@TableName("test_user")
@Data
@ToString
public class User extends Model<User> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Long roleId;

}
