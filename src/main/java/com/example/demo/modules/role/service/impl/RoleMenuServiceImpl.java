package com.example.demo.modules.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.modules.menu.entity.Menu;
import com.example.demo.modules.menu.service.MenuService;
import com.example.demo.modules.role.entity.RoleMenu;
import com.example.demo.modules.role.service.RoleMenuService;
import com.example.demo.modules.role.mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【role_menu】的数据库操作Service实现
* @createDate 2025-03-24 09:15:39
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

    @Autowired
    private MenuService service;

    @Override
    public void saveRoleMenus(String roleId, List<String> menuIds) {
        // 先删除该角色的所有菜单关系
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        remove(queryWrapper);

//        Set<String> ids = new HashSet<>();
//        menuIds.forEach(menuId -> {
//            Menu menu = service.getById(menuId);
//            if (menu != null && menu.getParentId() != null ) {
//                ids.add(menu.getParentId());
//            }
//        });

//        menuIds.addAll(ids);
        List<RoleMenu> roleMenus = menuIds.stream()
                .map(menuId -> {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(menuId);
                    roleMenu.setCreateTime(LocalDateTime.now());
                    return roleMenu;
                })
                .collect(Collectors.toList());
        saveBatch(roleMenus);

    }
}




