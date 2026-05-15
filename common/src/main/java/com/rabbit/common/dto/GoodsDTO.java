package com.rabbit.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品DTO
 */
@Data
public class GoodsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "商品标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "商品价格不能为空")
    @Positive(message = "商品价格必须大于0")
    private BigDecimal price;

    private BigDecimal originalPrice;

    private String images;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    private Long sellerId;

    @NotNull(message = "库存不能为空")
    private Integer stock;

    private Integer quality;

    private Integer tradeType;

    private String tradeLocation;
}

