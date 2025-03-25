package com.example.demo.modules.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.modules.company.entity.Company;
import com.example.demo.modules.company.mapper.CompanyMapper;
import com.example.demo.modules.company.service.CompanyService;
import com.example.demo.modules.menu.entity.Menu;
import com.example.demo.modules.menu.mapper.MenuMapper;
import com.example.demo.modules.role.entity.RoleMenu;
import com.example.demo.modules.role.mapper.RoleMenuMapper;
import com.example.demo.modules.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
* @author Administrator
* @description 针对表【company】的数据库操作Service实现
* @createDate 2025-03-11 16:45:52
*/
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
    private static final String SECRET_KEY_STRING = "3GOJK/1JIh1pVBebXz6xD0NACaG2A9V4I5wwS5cjxkk="; // 替换为您的固定密钥
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;

    @Override
    public Page<Company> getCompanyList(String companyName,Integer pageNo,Integer pageSize) {
        // 创建分页对象
        Page<Company> page = new Page<>(pageNo,pageSize);

        // 构建查询条件
        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<>();

        if (companyName!=null){
            // 添加查询条件
            queryWrapper.like( Company::getCompanyName, companyName);
        }

        // 执行查询
        return this.page(page, queryWrapper);
    }

    @Override
    public void getStatus() {
        // 1. 从请求头获取token
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("未登录");
        }
        // 去除 "Bearer " 前缀
        token = token.replace("Bearer ", "").trim();

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)  // 替换为你的密钥
                .parseClaimsJws(token)
                .getBody();
        String userId = claims.getSubject();
        if (userId == null) {
            throw new RuntimeException("token无效");
        }

        // 3. 获取用户的角色ID
        List<String> roleIds = userService.getUserRoleIds(userId);
        if (roleIds.isEmpty()) {
           return ;
        }

        // 4. 获取角色对应的所有菜单ID
        List<Long> menuIds = roleMenuMapper.getMenuIdsByRoleIds(roleIds);
        if (menuIds.isEmpty()) {
            return ;
        }

        menuIds.forEach(menuId -> {
            if (menuId == null) {
                return ;
            }
        });

        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleIds);
        queryWrapper.eq(RoleMenu::getMenuId, menuIds);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(queryWrapper);

        // 5. 获取所有菜单信息
        List<Menu> allMenus = menuMapper.selectBatchIds(menuIds);
    }
}




