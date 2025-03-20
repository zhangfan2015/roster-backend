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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/getTree")
    public Result<List<Map<String, Object>>> getTree() {
        try {
            List<Depart> allDeparts = departService.list();
            List<Map<String, Object>> treeList = buildTree(allDeparts);
            return Result.OK(treeList);
        } catch (Exception e) {
            return Result.error("获取部门树失败：" + e.getMessage());
        }
    }

    private List<Map<String, Object>> buildTree(List<Depart> departs) {
        Map<String, List<Depart>> parentIdMap = departs.stream()
                .collect(Collectors.groupingBy(d ->
                        d.getParentId() == null ? "" : d.getParentId()));

        return buildTreeNodes(parentIdMap, "");
    }

    private List<Map<String, Object>> buildTreeNodes(
            Map<String, List<Depart>> parentIdMap, String parentId) {
        List<Map<String, Object>> nodes = new ArrayList<>();

        List<Depart> children = parentIdMap.get(parentId);
        if (children != null) {
            for (Depart depart : children) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", depart.getId());
                node.put("key", depart.getId());
                node.put("title", depart.getDepartName());
                node.put("parentId", depart.getParentId());

                // 递归获取子节点
                List<Map<String, Object>> childNodes =
                        buildTreeNodes(parentIdMap, depart.getId());
                if (!childNodes.isEmpty()) {
                    node.put("children", childNodes);
                }

                nodes.add(node);
            }
        }

        return nodes;
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
