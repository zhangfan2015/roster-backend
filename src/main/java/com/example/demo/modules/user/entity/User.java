package com.example.demo.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description:
 */
@Data
@TableName("user")
@Accessors(chain = true)
public class User {
//    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    //用户名
    private String username;
    //密码
    private String password;
    //真实姓名
    private String realname;
//    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /**
     * 工号
     */
    private String workNo;
    private String sex;
    private Long age;
    private String phone;
    private String email;
    /**
     * 职位
     */
    private String positionId;
    private String positionName;
    /**
     * 部门
     */
    private String departmentId;
    private String departmentName;
    /**
     * 状态(1：正常  2：冻结 ）
     */
    private Integer status;
    /**
     * 删除状态（0，正常，1已删除）
     */
    private Integer delFlag;
    /**
     * 公司
     */
    private String companyId;
    private String companyName;
//
    @TableField(exist = false)
    private String fullName;

    private String img;
    /**
     *  角色id
     */
    private String roleId;
    private String roleName;
}
