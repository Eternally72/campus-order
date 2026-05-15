package com.rabbit.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rabbit.common.exception.BusinessException;
import com.rabbit.common.result.Result;
import com.rabbit.common.result.ResultCodeEnum;
import com.rabbit.common.vo.PageVO;
import com.rabbit.common.dto.OrderCreateDTO;
import com.rabbit.common.entity.Order;
import com.rabbit.common.feign.GoodsFeignClient;
import com.rabbit.common.utils.IdUtil;
import com.rabbit.common.vo.GoodsVO;
import com.rabbit.order.mapper.OrderMapper;
import com.rabbit.order.mq.OrderMessageProducer;
import com.rabbit.order.service.OrderService;
import com.rabbit.common.vo.OrderVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final GoodsFeignClient goodsFeignClient;
    private final OrderMessageProducer orderMessageProducer;

    @Override
    @GlobalTransactional(name = "create-order", rollbackFor = Exception.class)
    public Long createOrder(Long userId, OrderCreateDTO createDTO) {
        Result<GoodsVO> goodsDetailResult = goodsFeignClient.getGoodsDetail(createDTO.getGoodsId());
        if (goodsDetailResult == null || !ResultCodeEnum.SUCCESS.getCode().equals(goodsDetailResult.getCode())
                || goodsDetailResult.getData() == null) {
            throw new BusinessException(ResultCodeEnum.GOODS_NOT_EXIST);
        }

        GoodsVO goods = goodsDetailResult.getData();
        if (!Integer.valueOf(1).equals(goods.getStatus())) {
            throw new BusinessException(ResultCodeEnum.GOODS_OFF_SHELF);
        }
        if (goods.getSellerId() != null && goods.getSellerId().equals(userId)) {
            throw new BusinessException("不能购买自己发布的商品");
        }

        Result<Boolean> deductResult = goodsFeignClient.deductStock(createDTO.getGoodsId(), createDTO.getQuantity());
        if (deductResult == null || !ResultCodeEnum.SUCCESS.getCode().equals(deductResult.getCode())
                || !Boolean.TRUE.equals(deductResult.getData())) {
            String message = deductResult == null ? ResultCodeEnum.GOODS_STOCK_NOT_ENOUGH.getMessage() : deductResult.getMessage();
            throw new BusinessException(ResultCodeEnum.GOODS_STOCK_NOT_ENOUGH, message);
        }

        Order order = new Order();
        order.setId(IdUtil.nextId());
        order.setOrderNo(IdUtil.orderNo());
        order.setBuyerId(userId);
        order.setSellerId(goods.getSellerId());
        order.setGoodsId(createDTO.getGoodsId());
        order.setGoodsTitle(goods.getTitle());
        order.setGoodsImage(goods.getImages());
        order.setGoodsPrice(goods.getPrice());
        order.setQuantity(createDTO.getQuantity());
        order.setTotalAmount(goods.getPrice().multiply(BigDecimal.valueOf(createDTO.getQuantity())));
        order.setStatus(0); // 待付款
        order.setTradeType(createDTO.getTradeType() == null ? goods.getTradeType() : createDTO.getTradeType());
        order.setTradeLocation(createDTO.getTradeLocation() == null ? goods.getTradeLocation() : createDTO.getTradeLocation());
        order.setAddress(createDTO.getAddress());
        order.setPhone(createDTO.getPhone());
        order.setRemark(createDTO.getRemark());
        order.setDeleted(0);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        orderMapper.insert(order);

        orderMessageProducer.sendOrderCreatedMessage(order.getId(), order.getOrderNo(), order.getBuyerId(), order.getSellerId());

        log.info("创建订单成功: orderId={}, orderNo={}", order.getId(), order.getOrderNo());
        return order.getId();
    }

    @Override
    @GlobalTransactional(name = "cancel-order", rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, String reason) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCodeEnum.ORDER_NOT_EXIST);
        }
        if (order.getStatus() != 0) {
            throw new BusinessException(ResultCodeEnum.ORDER_STATUS_ERROR);
        }

        // 恢复库存
        goodsFeignClient.restoreStock(order.getGoodsId(), order.getQuantity());

        // 更新订单状态
        order.setStatus(4);
        order.setCancelTime(LocalDateTime.now());
        order.setCancelReason(reason);
        orderMapper.update(order);

        orderMessageProducer.sendOrderCancelledMessage(order.getId(), order.getOrderNo(), order.getBuyerId(), order.getSellerId());
        log.info("取消订单成功: orderId={}", orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCodeEnum.ORDER_NOT_EXIST);
        }
        if (order.getStatus() != 0) {
            throw new BusinessException(ResultCodeEnum.ORDER_STATUS_ERROR);
        }

        order.setStatus(1);
        order.setPayTime(LocalDateTime.now());
        orderMapper.update(order);

        orderMessageProducer.sendOrderPaidMessage(order.getId(), order.getOrderNo(), order.getBuyerId(), order.getSellerId());
        log.info("订单支付成功: orderId={}", orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deliverOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCodeEnum.ORDER_NOT_EXIST);
        }
        if (order.getStatus() != 1) {
            throw new BusinessException(ResultCodeEnum.ORDER_STATUS_ERROR);
        }

        order.setStatus(2);
        order.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(order);
        log.info("订单发货成功: orderId={}", orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCodeEnum.ORDER_NOT_EXIST);
        }
        if (order.getStatus() != 2) {
            throw new BusinessException(ResultCodeEnum.ORDER_STATUS_ERROR);
        }

        order.setStatus(3);
        order.setFinishTime(LocalDateTime.now());
        orderMapper.update(order);

        orderMessageProducer.sendOrderCompletedMessage(order.getId(), order.getOrderNo(), order.getBuyerId(), order.getSellerId());
        log.info("订单确认收货成功: orderId={}", orderId);
    }

    @Override
    public OrderVO getOrderDetail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCodeEnum.ORDER_NOT_EXIST);
        }
        return convertToVO(order);
    }

    @Override
    public PageVO<OrderVO> getMyOrders(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.selectByBuyerId(userId, status);
        PageInfo<Order> pageInfo = new PageInfo<>(orders);

        List<OrderVO> voList = orders.stream().map(this::convertToVO).collect(Collectors.toList());
        return PageVO.of(pageInfo.getTotal(), pageNum, pageSize, voList);
    }

    @Override
    public PageVO<OrderVO> getMySoldOrders(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.selectBySellerId(userId, status);
        PageInfo<Order> pageInfo = new PageInfo<>(orders);

        List<OrderVO> voList = orders.stream().map(this::convertToVO).collect(Collectors.toList());
        return PageVO.of(pageInfo.getTotal(), pageNum, pageSize, voList);
    }

    private OrderVO convertToVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setStatusDesc(getStatusDesc(order.getStatus()));
        return vo;
    }

    private String getStatusDesc(Integer status) {
        return switch (status) {
            case 0 -> "待付款";
            case 1 -> "待发货";
            case 2 -> "待收货";
            case 3 -> "已完成";
            case 4 -> "已取消";
            case 5 -> "已退款";
            default -> "未知状态";
        };
    }
}

