package com.example.demo.modules.position.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.modules.position.entity.Position;
import com.example.demo.modules.position.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/position")
public class PositionController {
    @Autowired
    private PositionService positionService;

    @PostMapping("/add")
    public Result<String> addPosition(@RequestBody Position position){
        position.setCreateTime(LocalDateTime.now());
        positionService.save(position);
        return Result.OK("添加成功！");
    }
    @GetMapping("/getPositionList")
    public Result<Page<Position>> getPositionList(String positionName,
                                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        Page<Position> page = positionService.getPositionList(positionName,pageNo,pageSize);
        return Result.OK(page);
    }



    @PutMapping("/edit")
    public Result<String> updatePosition(@RequestBody Position position) {
        position.setUpdateTime(LocalDateTime.now());
        positionService.updateById(position);
        return Result.OK("编辑成功！");
    }

    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        positionService.removeById(id);
        return Result.OK("删除成功!");
    }

    @PutMapping("/dongjie")
    public Result<String> dongjie(@RequestBody Position position) {

        if (position.getStatus() ==0){
            position.setStatus(1);
        }else {
            position.setStatus(0);
        }
        position.setUpdateTime(LocalDateTime.now());
        positionService.updateById(position);
        return Result.OK("编辑成功！");
    }
}
