package com.example.demo.modules.role.entity;

import lombok.Data;

import java.util.List;

@Data
public class RoleMenuVo {
    private String roleId;
    private List<String> menuIds;
}
