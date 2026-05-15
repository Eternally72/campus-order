package com.rabbit.common.entity;

import com.rabbit.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 通知记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NotifyRecord extends BaseEntity {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 通知类型（1-订单通知，2-系统通知，3-活动通知）
     */
    private Integer type;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 关联业务ID
     */
    private Long bizId;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 是否已读（0-未读，1-已读）
     */
    private Integer readFlag;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 发送状态（0-待发送，1-已发送，2-发送失败）
     */
    private Integer sendStatus;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 失败原因
     */
    private String failReason;
}

