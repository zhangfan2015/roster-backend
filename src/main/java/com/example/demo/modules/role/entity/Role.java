package com.example.demo.modules.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @TableName role
 */
@TableName(value ="role")
@Data
@Accessors(chain = true)
public class Role {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String roleName;

    private String roleCode;
    /**
     * 备注
     */
    private String remark;
    /**
     * 删除状态（0，正常，1已删除）
     */
    private Integer delFlag;
}