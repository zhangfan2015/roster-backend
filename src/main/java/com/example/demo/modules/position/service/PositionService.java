package com.example.demo.modules.position.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.modules.position.entity.Position;

/**
* @author Administrator
* @description 针对表【position】的数据库操作Service
* @createDate 2025-03-12 11:14:42
*/
public interface PositionService extends IService<Position> {
//
    Page<Position> getPositionList(Position position, Integer pageNo, Integer pageSize);



}
