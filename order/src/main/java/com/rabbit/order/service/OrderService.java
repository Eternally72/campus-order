package com.rabbit.order.service;

import com.rabbit.common.vo.PageVO;
import com.rabbit.common.dto.OrderCreateDTO;
import com.rabbit.common.vo.OrderVO;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单（分布式事务）
     */
    Long createOrder(Long userId, OrderCreateDTO createDTO);

    /**
     * 取消订单
     */
    void cancelOrder(Long orderId, String reason);

    /**
     * 支付订单
     */
    void payOrder(Long orderId);

    /**
     * 发货
     */
    void deliverOrder(Long orderId);

    /**
     * 确认收货
     */
    void confirmOrder(Long orderId);

    /**
     * 获取订单详情
     */
    OrderVO getOrderDetail(Long orderId);

    /**
     * 查询我的订单（买家）
     */
    PageVO<OrderVO> getMyOrders(Long userId, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 查询我的卖出订单（卖家）
     */
    PageVO<OrderVO> getMySoldOrders(Long userId, Integer status, Integer pageNum, Integer pageSize);
}

