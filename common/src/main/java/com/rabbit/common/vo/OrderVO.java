package com.rabbit.common.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单VO
 */
@Data
public class OrderVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String orderNo;

    private Long buyerId;

    private String buyerName;

    private Long sellerId;

    private String sellerName;

    private Long goodsId;

    private String goodsTitle;

    private String goodsImage;

    private BigDecimal goodsPrice;

    private Integer quantity;

    private BigDecimal totalAmount;

    private Integer status;

    private String statusDesc;

    private Integer tradeType;

    private String tradeLocation;

    private String address;

    private String phone;

    private String remark;

    private LocalDateTime payTime;

    private LocalDateTime deliveryTime;

    private LocalDateTime finishTime;

    private LocalDateTime cancelTime;

    private String cancelReason;

    private LocalDateTime createTime;
}

