package com.rabbit.common.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户收藏实体
 */
@Data
public class Favorite {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    private Integer deleted;
}
