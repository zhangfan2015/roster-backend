package com.example.demo.modules.role.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.modules.company.entity.Company;
import com.example.demo.modules.depart.entity.Depart;
import com.example.demo.modules.role.entity.Role;
import com.example.demo.modules.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/addRole")
    public Result<String> addRole(@RequestBody Role role){
        role.setCreateTime(LocalDateTime.now());
        roleService.save(role);
        return Result.OK("添加成功！");
    }


    @GetMapping("/getRoleList")
	public Result<Page<Role>> getRoleList(Role role,
                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Page<Role> page = roleService.getRoleList(role,pageNo,pageSize);
		return Result.OK(page);
	}



    @PutMapping("/edit")
    public Result<String> updateRole(@RequestBody Role role) {
        role.setUpdateTime(LocalDateTime.now());
        roleService.updateById(role);
        return Result.OK("编辑成功！");
    }

    @DeleteMapping("delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        roleService.removeById(id);
        return Result.OK("删除成功!");
    }

    @PutMapping("/dongjie")
    public Result<String> dongjie(@RequestBody Role role) {

        if (role.getStatus() ==0){
            role.setStatus(1);
        }else {
            role.setStatus(0);
        }
        role.setUpdateTime(LocalDateTime.now());
        roleService.updateById(role);
        return Result.OK("编辑成功！");
    }
}
