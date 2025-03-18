package com.example.demo.modules.depart.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @TableName depart
 */
@TableName(value ="depart")
@Data
@Accessors(chain = true)
public class Depart {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String departName;
    /**
     * 上级部门/父id
     */
    private String parentId;

    private String departCode;
    /**
     * 公司
     */
    private String companyId;

    private String companyName;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 传真
     */
    private String fax;
    private String address;
    /**
     * 状态(0：启用  1：不启用 ）
     */
    private Integer status;
    /**
     * 删除状态（0，正常，1已删除）
     */
    private Integer delFlag;
}