package com.example.demo.modules.menu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Result;
import com.example.demo.modules.menu.entity.Menu;
import com.example.demo.modules.menu.service.MenuService;
import com.example.demo.modules.menu.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;


    // 获取菜单树
    @GetMapping("/tree")
    public Result<List<MenuVo>> getMenuTree() {
        List<MenuVo> tree = menuService.getMenuTree();
        return Result.success(tree);
    }

    @PostMapping("/add")
    public Result<String> addMenu(@RequestBody Menu menu){
        menu.setCreateTime(LocalDateTime.now());
        menuService.save(menu);
        return Result.OK("添加成功！");
    }

    @GetMapping("/getCurrentUserMenus")
    public Result getCurrentUserMenus() {
        List<MenuVo> menus = menuService.getCurrentUserMenus();
        return Result.success(menus);
    }

    @GetMapping("/getMenuList")
    public Result<List<Menu>> getMenuList(@RequestParam(name="menuName",required=false)String menuName){
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Menu::getMenuName,menuName);
        List<Menu> menuList = menuService.list(queryWrapper);

        return Result.OK(menuList);
    }

    @PutMapping("/edit")
    public Result<String> updateMenu(@RequestBody Menu menu) {
        menu.setUpdateTime(LocalDateTime.now());
        menuService.updateById(menu);
        return Result.OK("编辑成功！");
    }

    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        menuService.removeById(id);
        return Result.OK("删除成功!");
    }
}
