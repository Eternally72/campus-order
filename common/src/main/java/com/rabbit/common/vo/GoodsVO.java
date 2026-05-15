package com.rabbit.common.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品VO
 */
@Data
public class GoodsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String images;

    private Long categoryId;

    private String categoryName;

    private Long sellerId;

    private String sellerName;

    private Integer stock;

    private Integer quality;

    private Integer tradeType;

    private String tradeLocation;

    private Integer status;

    private Integer viewCount;

    private Integer favoriteCount;

    private LocalDateTime createTime;
}

