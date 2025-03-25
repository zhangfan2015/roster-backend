package com.example.demo.modules.menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.util.JwtUtil;
import com.example.demo.modules.menu.entity.Menu;
import com.example.demo.modules.menu.mapper.MenuMapper;
import com.example.demo.modules.menu.service.MenuService;
import com.example.demo.modules.menu.vo.MenuVo;
import com.example.demo.modules.role.mapper.RoleMenuMapper;
import com.example.demo.modules.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    private static final String SECRET_KEY_STRING = "3GOJK/1JIh1pVBebXz6xD0NACaG2A9V4I5wwS5cjxkk="; // 替换为您的固定密钥
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());



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

    @Override
    public List<MenuVo> getCurrentUserMenus() {
        // 1. 从请求头获取token
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("未登录");
        }
        // 去除 "Bearer " 前缀
        token = token.replace("Bearer ", "").trim();

        // 2. 从token中解析用户ID
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)  // 替换为你的密钥
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (io.jsonwebtoken.security.SignatureException e) {
            throw new RuntimeException("JWT签名不匹配，无法验证JWT的有效性。密钥: " + SECRET_KEY, e);
        }
        catch (Exception e) {
            throw new RuntimeException("token无效", e);
        }

        String userId = claims.getSubject();
        if (userId == null) {
            throw new RuntimeException("token无效");
        }

        // 3. 获取用户的所有角色ID
        List<String> roleIds = userService.getUserRoleIds(userId);
        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 4. 获取角色对应的所有菜单ID
        List<Long> menuIds = roleMenuMapper.getMenuIdsByRoleIds(roleIds);
        if (menuIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 5. 获取所有菜单信息
        List<Menu> allMenus = menuMapper.selectBatchIds(menuIds);

        // 5. 构建树形结构
        return buildTree(allMenus);
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




