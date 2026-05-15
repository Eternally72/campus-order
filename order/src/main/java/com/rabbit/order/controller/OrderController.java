package com.rabbit.order.controller;

import com.rabbit.common.result.Result;
import com.rabbit.common.vo.PageVO;
import com.rabbit.common.dto.OrderCreateDTO;
import com.rabbit.order.service.OrderService;
import com.rabbit.common.vo.OrderVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping
    public Result<Long> createOrder(@RequestHeader("X-User-Id") Long userId,
                                     @Valid @RequestBody OrderCreateDTO createDTO) {
        Long orderId = orderService.createOrder(userId, createDTO);
        return Result.success(orderId);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        OrderVO orderVO = orderService.getOrderDetail(id);
        return Result.success(orderVO);
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel/{id}")
    public Result<Void> cancelOrder(@PathVariable Long id, @RequestParam(required = false) String reason) {
        orderService.cancelOrder(id, reason);
        return Result.success();
    }

    /**
     * 支付订单
     */
    @PostMapping("/pay/{id}")
    public Result<Void> payOrder(@PathVariable Long id) {
        orderService.payOrder(id);
        return Result.success();
    }

    /**
     * 发货
     */
    @PostMapping("/deliver/{id}")
    public Result<Void> deliverOrder(@PathVariable Long id) {
        orderService.deliverOrder(id);
        return Result.success();
    }

    /**
     * 确认收货
     */
    @PostMapping("/confirm/{id}")
    public Result<Void> confirmOrder(@PathVariable Long id) {
        orderService.confirmOrder(id);
        return Result.success();
    }

    /**
     * 查询我的订单（买家）
     */
    @GetMapping("/my")
    public Result<PageVO<OrderVO>> getMyOrders(@RequestHeader("X-User-Id") Long userId,
                                                @RequestParam(required = false) Integer status,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<OrderVO> pageVO = orderService.getMyOrders(userId, status, pageNum, pageSize);
        return Result.success(pageVO);
    }

    /**
     * 查询我的卖出订单（卖家）
     */
    @GetMapping("/sold")
    public Result<PageVO<OrderVO>> getMySoldOrders(@RequestHeader("X-User-Id") Long userId,
                                                    @RequestParam(required = false) Integer status,
                                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<OrderVO> pageVO = orderService.getMySoldOrders(userId, status, pageNum, pageSize);
        return Result.success(pageVO);
    }
}

