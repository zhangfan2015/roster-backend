package com.example.demo.modules.role.mapper;

import com.example.demo.modules.role.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【role_menu】的数据库操作Mapper
* @createDate 2025-03-24 09:15:39
* @Entity com.example.demo.modules.role.entity.RoleMenu
*/
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 根据角色ID列表获取菜单ID列表
     */
    List<Long> getMenuIdsByRoleIds(@Param("roleIds") List<String> roleIds);
}




