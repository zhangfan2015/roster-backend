package com.example.demo.modules.depart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Result;
import com.example.demo.modules.depart.entity.Depart;
import com.example.demo.modules.depart.mapper.DepartMapper;
import com.example.demo.modules.depart.service.DepartService;
import com.example.demo.modules.message.entity.InitMessage;
import com.example.demo.modules.message.service.InitMessageService;
import com.example.demo.modules.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【depart】的数据库操作Service实现
* @createDate 2025-03-12 10:02:40
*/
@Service
public class DepartServiceImpl extends ServiceImpl<DepartMapper, Depart> implements DepartService {


}




