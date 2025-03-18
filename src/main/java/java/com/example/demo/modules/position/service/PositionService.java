package java.com.example.demo.modules.position.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【position】的数据库操作Service
* @createDate 2025-03-12 11:14:42
*/
public interface PositionService extends IService<Position> {
//
    Page<Position> getPositionList(String positionName, Integer pageNo, Integer pageSize);



}
