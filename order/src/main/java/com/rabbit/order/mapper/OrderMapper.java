package com.rabbit.order.mapper;

import com.rabbit.common.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单Mapper
 */
@Mapper
public interface OrderMapper {

    /**
     * 根据ID查询订单
     */
    Order selectById(@Param("id") Long id);

    /**
     * 根据订单号查询订单
     */
    Order selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 插入订单
     */
    int insert(Order order);

    /**
     * 更新订单
     */
    int update(Order order);

    /**
     * 查询买家订单列表
     */
    List<Order> selectByBuyerId(@Param("buyerId") Long buyerId, @Param("status") Integer status);

    /**
     * 查询卖家订单列表
     */
    List<Order> selectBySellerId(@Param("sellerId") Long sellerId, @Param("status") Integer status);
}

