package com.example.demo.modules.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.modules.message.entity.InitMessage;
import com.example.demo.modules.message.mapper.InitMessageMapper;
import com.example.demo.modules.message.service.InitMessageService;
import com.example.demo.modules.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author Administrator
* @description 针对表【init_message】的数据库操作Service实现
* @createDate 2025-03-20 14:23:28
*/
@Service
public class InitMessageServiceImpl extends ServiceImpl<InitMessageMapper, InitMessage>
    implements InitMessageService {

    @Override
    public Page<InitMessage> getMessages(InitMessage message, Integer pageNo, Integer pageSize) {
        Page<InitMessage> page = new Page<>(pageNo, pageSize);
        // 构建查询条件
        LambdaQueryWrapper<InitMessage> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        if (StringUtils.hasText(message.getTitle())) {
            queryWrapper.like(InitMessage::getTitle, message.getTitle());
        }
        // 确保 del_flag = 0 条件一定会被添加
        return this.page(page, queryWrapper);
    }
}




