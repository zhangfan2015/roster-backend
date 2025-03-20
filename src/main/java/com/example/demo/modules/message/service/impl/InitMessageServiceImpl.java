package com.example.demo.modules.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.modules.message.entity.InitMessage;
import com.example.demo.modules.message.mapper.InitMessageMapper;
import com.example.demo.modules.message.service.InitMessageService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【init_message】的数据库操作Service实现
* @createDate 2025-03-20 14:23:28
*/
@Service
public class InitMessageServiceImpl extends ServiceImpl<InitMessageMapper, InitMessage>
    implements InitMessageService {

}




