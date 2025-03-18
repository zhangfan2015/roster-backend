package com.example.demo.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @TableName user_role
 */
@TableName(value ="user_role")
@Data
@Accessors(chain = true)
public class UserRole {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String userId;
//
    private String roleId;
}