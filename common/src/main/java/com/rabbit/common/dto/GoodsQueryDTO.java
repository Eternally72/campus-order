package com.rabbit.common.dto;

import com.rabbit.common.dto.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsQueryDTO extends PageQueryDTO {

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 卖家ID
     */
    private Long sellerId;

    /**
     * 最低价格
     */
    private BigDecimal minPrice;

    /**
     * 最高价格
     */
    private BigDecimal maxPrice;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 成色
     */
    private Integer quality;
}

