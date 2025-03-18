package java.com.example.demo.modules.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.modules.menu.entity.Menu;
import com.example.demo.modules.menu.vo.MenuVo;

import java.util.List;

/**
* @author Administrator
* @description 针对表【menu】的数据库操作Service
* @createDate 2025-03-13 14:45:31
*/
public interface MenuService extends IService<Menu> {

    List<MenuVo> getMenuTree();

}
