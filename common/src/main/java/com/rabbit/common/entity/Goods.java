package com.rabbit.common.entity;

import com.rabbit.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Goods extends BaseEntity {

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 商品图片（JSON数组）
     */
    private String images;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 卖家ID
     */
    private Long sellerId;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 成色（1-全新，2-几乎全新，3-轻微使用痕迹，4-明显使用痕迹）
     */
    private Integer quality;

    /**
     * 交易方式（1-线下交易，2-邮寄，3-都可以）
     */
    private Integer tradeType;

    /**
     * 交易地点
     */
    private String tradeLocation;

    /**
     * 状态（0-下架，1-在售，2-已售出）
     */
    private Integer status;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 收藏量
     */
    private Integer favoriteCount;
}

