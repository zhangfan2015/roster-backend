package com.example.demo.modules.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.Result;
import com.example.demo.modules.user.entity.User;

import java.util.List;
import java.util.Map;

/**
 */
public interface UserService extends IService<User> {
    User login(String username, String password);
    boolean register(User user);
    /**
     * 根据角色Id查询
     * @param page
     * @param roleId 角色id
     * @param username 用户账户名称
     * @return
     */
    public IPage<User> getUserByRoleId(Page<User> page, String roleId, String username);

    /**
     * 根据 userIds查询，查询用户所属部门的名称（多个部门名逗号隔开）gg
     * @param userIds
     * @return
     */
    public Map<String,String> getDepNamesByUserIds(List<String> userIds);

    Page<User> getUserList(User user, Integer pageNo, Integer pageSize);

    Result<?> changePassword(User u);

    Page<User> getYxUserList(Integer pageNo, Integer pageSize);
}
