package com.example.demo.modules.menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.modules.menu.entity.Menu;
import com.example.demo.modules.menu.mapper.MenuMapper;
import com.example.demo.modules.menu.service.MenuService;
import com.example.demo.modules.menu.vo.MenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author Administrator
* @description 针对表【menu】的数据库操作Service实现
* @createDate 2025-03-13 14:45:31
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<MenuVo> getMenuTree() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件
        queryWrapper.orderByAsc(Menu::getSort);
        // 只查询未删除的菜单
        queryWrapper.eq(Menu::getDelFlag, 0);
        List<Menu> allMenus = menuMapper.selectList(queryWrapper);
        if (allMenus == null || allMenus.isEmpty()) {
            return new ArrayList<>();
        }
        // 构建树形结构
        List<MenuVo> tree = buildTree(allMenus);
        return tree;
    }


    private List<MenuVo> buildTree(List<Menu> menus) {
        List<MenuVo> tree = new ArrayList<>();
        Map<String, MenuVo> menuMap = new HashMap<>();
        // 转换所有菜单为VO对象
        for (Menu menu : menus) {
            MenuVo vo = convertToVO(menu);
            menuMap.put(menu.getId(), vo);
        }
        // 构建树形结构
        for (Menu menu : menus) {
            MenuVo current = menuMap.get(menu.getId());
            if (menu.getParentId() == null || menu.getParentId().trim().isEmpty() || "0".equals(menu.getParentId())){
                // 根节点
                tree.add(current);
            } else {
                // 子节点
                MenuVo parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(current);
//                    tree.add(parent);
                }
            }
        }
        // 对树中的每个节点的子节点进行排序
        sortTree(tree);
        return tree;
    }

    private void sortTree(List<MenuVo> menuList) {
        if (menuList == null || menuList.isEmpty()) {
            return;
        }

        // 根据sort字段排序
        menuList.sort(Comparator.comparing(MenuVo::getSort, Comparator.nullsLast(Comparator.naturalOrder())));

        // 递归排序子节点
        for (MenuVo menu : menuList) {
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                sortTree(menu.getChildren());
            }
        }
    }

    private MenuVo convertToVO(Menu menu) {
        MenuVo vo = new MenuVo();
        BeanUtils.copyProperties(menu, vo);
        return vo;
    }
}




