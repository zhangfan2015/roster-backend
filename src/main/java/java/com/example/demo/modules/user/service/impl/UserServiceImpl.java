package java.com.example.demo.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.modules.user.entity.User;
import com.example.demo.modules.user.mapper.UserMapper;
import com.example.demo.modules.user.service.UserService;
import com.example.demo.modules.user.vo.UserDepVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        IPage<User> userRoleList = userMapper.getUserByRoleId(page, roleId, username);
        List<User> records = userRoleList.getRecords();
        if (null != records && records.size() > 0) {
            List<String> userIds = records.stream().map(User::getId).collect(Collectors.toList());
            Map<String, String> useDepNames = this.getDepNamesByUserIds(userIds);
            for (User sysUser : userRoleList.getRecords()) {
                //设置部门
                sysUser.setDepartmentName(useDepNames.get(sysUser.getId()));
                //设置用户职位id
//                this.userPositionId(sysUser);
            }
        }
        return userRoleList;
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
}
