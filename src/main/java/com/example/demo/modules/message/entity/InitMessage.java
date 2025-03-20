package com.example.demo.modules.message.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName init_message
 */
@Data
public class InitMessage {
    /**
     * 
     */
    private String id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 标题
     */
    private String title;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 发送时间
     */
    private LocalDateTime createTime;

    /**
     * 消息状态(已读，未读)
     */
    private String status;

    /**
     * 消息链接
     */
    private String url;
}