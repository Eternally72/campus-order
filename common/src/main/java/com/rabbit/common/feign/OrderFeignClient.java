package com.rabbit.common.feign;

import com.rabbit.common.result.Result;
import com.rabbit.common.vo.OrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 订单服务Feign接口（公共）
 */
@FeignClient(name = "order-service", contextId = "commonOrderFeignClient")
public interface OrderFeignClient {

    /**
     * 获取订单详情
     */
    @GetMapping("/order/{id}")
    Result<OrderVO> getOrderDetail(@PathVariable Long id);
}

