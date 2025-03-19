package com.example.demo.modules.position.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.modules.position.entity.Position;
import com.example.demo.modules.position.mapper.PositionMapper;
import com.example.demo.modules.position.service.PositionService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【position】的数据库操作Service实现
* @createDate 2025-03-12 11:14:42
*/
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {

    @Override
    public Page<Position> getPositionList(String positionName, Integer pageNo, Integer pageSize) {
        Page<Position> page = new Page<>(pageNo,pageSize);

        // 构建查询条件
        LambdaQueryWrapper<Position> queryWrapper = new LambdaQueryWrapper<>();

        if (positionName!=null){
            // 添加查询条件
            queryWrapper.like( Position::getPositionName, positionName);
        }

        // 执行查询
        return this.page(page, queryWrapper);
    }
}




