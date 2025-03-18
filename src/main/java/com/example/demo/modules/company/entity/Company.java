package com.example.demo.modules.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @TableName company
 */
@Accessors(chain = true)
@TableName(value ="company")
@Data
public class Company {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 公司类型
     */
    private String companyType;

    private String tel;

    private String address;

    private String email;
    /**
     * 成立日期
     */
    private LocalDate clTime;
    /**
     * 公司简介
     */
    private String content;
    /**
     * 注册资本
     */
    private String registCapital;
    /**
     * 法人
     */
    private String people;
    /**
     * 状态(0：启用  1：不启用 ）
     */
    private Integer status;
    /**
     * 删除状态（0，正常，1已删除）
     */
    private Integer delFlag;
}