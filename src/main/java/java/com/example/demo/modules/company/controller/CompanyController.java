package java.com.example.demo.modules.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.modules.company.entity.Company;
import com.example.demo.modules.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping("/add")
    public Result<String> addCompany(@RequestBody Company company) {
        // 获取当前日期和时间
        company.setCreateTime(LocalDateTime.now());
        companyService.save(company);
        return Result.OK("添加成功！");
    }


    @GetMapping("/getCompanyList")
    public Result<Page<Company>> getCompanyList(String companyName,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        Page<Company> page = companyService.getCompanyList(companyName,pageNo,pageSize);
        return Result.OK(page);
    }

    @PutMapping("/edit")
    public Result<String> updateCompany(@RequestBody Company company) {
        company.setUpdateTime(LocalDateTime.now());
        companyService.updateById(company);
        return Result.OK("编辑成功！");
    }

    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        companyService.removeById(id);
        return Result.OK("删除成功!");
    }
    @PutMapping("/dongjie")
    public Result<String> dongjie(@RequestBody Company company) {

        if (company.getStatus() ==0){
            company.setStatus(1);
        }else {
            company.setStatus(0);
        }
        company.setUpdateTime(LocalDateTime.now());
        companyService.updateById(company);
        return Result.OK("编辑成功！");
    }

}
