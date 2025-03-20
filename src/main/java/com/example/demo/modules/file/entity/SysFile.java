package com.example.demo.modules.file.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName sys_file
 */
@Data
public class SysFile {
    /**
     * 
     */
    private String id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 是否删除
     */
    private Integer deleted;
}