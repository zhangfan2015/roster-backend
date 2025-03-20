package com.example.demo.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Result;
import com.example.demo.modules.user.entity.User;
import com.example.demo.modules.user.mapper.UserMapper;
import com.example.demo.modules.user.service.UserService;
import com.example.demo.modules.user.vo.UserDepVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        wrapper.eq(User::getPassword, DigestUtils.md5DigestAsHex(password.getBytes()));
        return getOne(wrapper);
    }

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getFullName());
        if (count(wrapper) > 0) {
            return false;
        }
//
        // 设置创建时间和更新时间
        user.setCreateTime(LocalDateTime.now());
//        user.setUpdateTime(LocalDateTime.now());
        user.setEmail(user.getEmail());
        // 密码加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setUsername(user.getFullName());
        return save(user);
    }

    @Override
    public IPage<User> getUserByRoleId(Page<User> page, String roleId, String username) {

        IPage<User> list = userMapper.getUserByRoleId(page, roleId, username);
        return list;
    }

    @Override
    public Map<String, String> getDepNamesByUserIds(List<String> userIds) {
        List<UserDepVo> list = this.baseMapper.getDepNamesByUserIds(userIds);

        Map<String, String> res = new HashMap(5);
        list.forEach(item -> {
                    if (res.get(item.getUserId()) == null) {
                        res.put(item.getUserId(), item.getDepartName());
                    } else {
                        res.put(item.getUserId(), res.get(item.getUserId()) + "," + item.getDepartName());
                    }
                }
        );
        return res;
    }

    @Override
    public Page<User> getUserList(User user, Integer pageNo, Integer pageSize) {
        Page<User> page = new Page<>(pageNo, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        if (StringUtils.hasText(user.getUsername())) {
            queryWrapper.like(User::getUsername, user.getUsername());
        }
        if (StringUtils.hasText(user.getRealname())) {
            queryWrapper.like(User::getRealname, user.getRealname());
        }
        // 确保 del_flag = 0 条件一定会被添加
        queryWrapper.eq(User::getDelFlag, 0);
        // 执行查询
        return this.page(page, queryWrapper);
    }

    @Override
    public Result<?> changePassword(User u) {

        u.setPassword(DigestUtils.md5DigestAsHex(u.getPassword().getBytes()));
        userMapper.updateById(u);
        return Result.ok("密码修改成功!");
    }

    @Override
    public Page<User> getYxUserList(Integer pageNo, Integer pageSize) {
        Page<User> page = new Page<>(pageNo, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getDelFlag, 0);//delflag正常
        queryWrapper.eq(User::getStatus, 1);//status正常
        // 执行查询
        return this.page(page, queryWrapper);
    }
}
