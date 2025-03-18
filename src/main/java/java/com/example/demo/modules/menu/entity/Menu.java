package java.com.example.demo.modules.menu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @TableName menu
 */
@TableName(value ="menu")
@Data
@Accessors(chain = true)
public class Menu {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String menuName;
    /**
     * 上级id
     */
    private String parentId;

    private Integer menuType;
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
}