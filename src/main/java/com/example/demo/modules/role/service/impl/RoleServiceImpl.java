package com.example.demo.modules.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.modules.role.entity.Role;
import com.example.demo.modules.role.mapper.RoleMapper;
import com.example.demo.modules.role.service.RoleService;
import com.example.demo.modules.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author Administrator
* @description 针对表【role】的数据库操作Service实现
* @createDate 2025-03-12 11:49:16
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public Page<Role> getRoleList(Role role, Integer pageNo, Integer pageSize) {
        Page<Role> page = new Page<>(pageNo, pageSize);
        // 构建查询条件
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        if (StringUtils.hasText(role.getRoleName())) {
            queryWrapper.like(Role::getRoleName, role.getRoleName());
        }
        if (StringUtils.hasText(role.getRoleCode())) {
            queryWrapper.like(Role::getRoleCode, role.getRoleCode());
        }
        // 确保 del_flag = 0 条件一定会被添加
        queryWrapper.eq(Role::getDelFlag, 0);
        // 执行查询
        return this.page(page, queryWrapper);
    }
}




