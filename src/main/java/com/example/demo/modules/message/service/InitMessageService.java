package com.example.demo.modules.message.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.modules.message.entity.InitMessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【init_message】的数据库操作Service
* @createDate 2025-03-20 14:23:28
*/
public interface InitMessageService extends IService<InitMessage> {

    Page<InitMessage> getMessages(InitMessage message, Integer pageNo, Integer pageSize);
}
