package com.example.demo.modules.position.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @TableName position
 */
@TableName(value ="position")
@Data
@Accessors(chain = true)
public class Position {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    /**
     * 职位名称
     **/
    private String positionName;
    /**
     * 部门
     **/
    private String departId;
    private String departName;

    private String departCode;
    /**
     * 公司
     **/
    private String companyId;

    private String companyName;
    /**
     * 状态(0：启用  1：不启用 ）
     */
    private Integer status;
    /**
     * 删除状态（0，正常，1已删除）
     */
    private Integer delFlag;
}