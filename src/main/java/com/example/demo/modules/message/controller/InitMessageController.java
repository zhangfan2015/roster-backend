package com.example.demo.modules.message.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.modules.message.entity.InitMessage;
import com.example.demo.modules.message.service.InitMessageService;
import com.example.demo.modules.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class InitMessageController {

    @Autowired
    private InitMessageService initMessageService;

    @PostMapping("/add")
    public Result<String> addMessage(@RequestBody InitMessage message) {

        initMessageService.save(message);
        return Result.OK("添加成功！");
    }

    @GetMapping("/getMessageList")
    public Result<?> getMessageList(@RequestParam("type") String type) {
        LambdaQueryWrapper<InitMessage> queryWrapper = new LambdaQueryWrapper<>();
        if (type!=null && type.equals("unread")){
            queryWrapper.eq(InitMessage::getStatus,"未读");
        }
        List<InitMessage> list = initMessageService.list(queryWrapper);
        return Result.OK(list);
    }

    @GetMapping("/getMessages")
    public Result<Page<InitMessage>> getMessages(InitMessage message,
                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        Page<InitMessage> page = initMessageService.getMessages(message,pageNo,pageSize);
        return Result.OK(page);
    }

    @PostMapping("/readAll")
    public Result<String> readAll() {
        LambdaQueryWrapper<InitMessage> queryWrapper = new LambdaQueryWrapper<>();
        List<InitMessage> list = initMessageService.list(queryWrapper);
        for (InitMessage message : list) {
            message.setStatus("已读");
            initMessageService.updateById(message);
        }
        return Result.OK("添加成功！");
    }

    @GetMapping("/getMessageDetail")
    public Result<?> getMessageDetail(@RequestParam("id") String id) {

        InitMessage byId = initMessageService.getById(id);

        return Result.OK(byId);
    }

}
