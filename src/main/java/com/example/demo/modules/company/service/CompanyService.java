package com.example.demo.modules.company.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.modules.company.entity.Company;

/**
* @author Administrator
* @description 针对表【company】的数据库操作Service
* @createDate 2025-03-11 16:45:52
*/
public interface CompanyService extends IService<Company> {

    Page<Company> getCompanyList(String companyName,Integer pageNo,Integer pageSize);

}
