package java.com.example.demo.modules.menu.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MenuVo {

    private String id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String menuName;
    /**
     * 上级id
     */
    private String parentId;

    private String menuType;
    /**
     * 图标
     */
    private String icon;
    /**
     *路径
     */
    private String url;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 删除状态（0，正常，1已删除）
     */
    private Integer delFlag;

    private List<MenuVo> children;  // 子菜单列表

}
