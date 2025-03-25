package com.example.demo.modules.role.service;

import com.example.demo.modules.role.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【role_menu】的数据库操作Service
* @createDate 2025-03-24 09:15:39
*/
public interface RoleMenuService extends IService<RoleMenu> {

    void saveRoleMenus(String roleId, List<String> menuIds);
}
