package com.example.demo.modules.depart.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Result;
import com.example.demo.modules.company.entity.Company;
import com.example.demo.modules.depart.entity.Depart;
import com.example.demo.modules.depart.service.DepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/depart")
public class DepartController {

    @Autowired
    private DepartService departService;

    @PostMapping("/add")
    public Result<String> addDepart(@RequestBody Depart depart){
        depart.setCreateTime(LocalDateTime.now());
        departService.save(depart);
        return Result.OK("添加成功！");
    }

    @GetMapping("/getDepartList")
    public List<Depart> getDepartList(){
        QueryWrapper<Depart> queryWrapper = new QueryWrapper<>();
        List<Depart> departs = departService.list(queryWrapper);
        return departs;
    }

    @PutMapping("/edit")
    public Result<String> updateDepart(@RequestBody Depart depart) {
        depart.setUpdateTime(LocalDateTime.now());
        departService.updateById(depart);
        return Result.OK("编辑成功！");
    }

    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        departService.removeById(id);
        return Result.OK("删除成功!");
    }

    @GetMapping("/getDepartByCompanyId")
    public List<Depart> getDepartByCompanyId(@RequestParam(name="companyId",required=true) String companyId){
        LambdaQueryWrapper<Depart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Depart::getCompanyId,companyId);
        List<Depart> departs = departService.list(queryWrapper);
        return departs;
    }

}
