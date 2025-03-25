package com.example.demo.modules.role.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName role_menu
 */
@Data
public class RoleMenu {
    /**
     * 
     */
    private String id;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 菜单id
     */
    private String menuId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}