package com.example.demo.modules.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.modules.company.entity.Company;
import com.example.demo.modules.company.mapper.CompanyMapper;
import com.example.demo.modules.company.service.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
* @author Administrator
* @description 针对表【company】的数据库操作Service实现
* @createDate 2025-03-11 16:45:52
*/
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

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
}




