package com.rabbit.order.mq;

import com.rabbit.common.constant.RabbitMQConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单消息生产者
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送订单创建消息
     */
    public void sendOrderCreatedMessage(Long orderId, String orderNo, Long buyerId, Long sellerId) {
        sendOrderMessage("ORDER_CREATED", orderId, orderNo, buyerId, sellerId);
        log.info("发送订单创建消息: orderId={}, orderNo={}", orderId, orderNo);
    }

    /**
     * 发送订单取消消息
     */
    public void sendOrderCancelledMessage(Long orderId, String orderNo, Long buyerId, Long sellerId) {
        sendOrderMessage("ORDER_CANCELLED", orderId, orderNo, buyerId, sellerId);
        log.info("发送订单取消消息: orderId={}, orderNo={}", orderId, orderNo);
    }

    /**
     * 发送订单支付成功消息
     */
    public void sendOrderPaidMessage(Long orderId, String orderNo, Long buyerId, Long sellerId) {
        sendOrderMessage("ORDER_PAID", orderId, orderNo, buyerId, sellerId);
        log.info("发送订单支付成功消息: orderId={}, orderNo={}", orderId, orderNo);
    }

    /**
     * 发送订单完成消息
     */
    public void sendOrderCompletedMessage(Long orderId, String orderNo, Long buyerId, Long sellerId) {
        sendOrderMessage("ORDER_COMPLETED", orderId, orderNo, buyerId, sellerId);
        log.info("发送订单完成消息: orderId={}, orderNo={}", orderId, orderNo);
    }

    private void sendOrderMessage(String type, Long orderId, String orderNo, Long buyerId, Long sellerId) {
        Map<String, Object> message = new HashMap<>();
        message.put("orderId", orderId);
        message.put("orderNo", orderNo);
        message.put("buyerId", buyerId);
        message.put("sellerId", sellerId);
        message.put("type", type);
        message.put("timestamp", System.currentTimeMillis());

        rabbitTemplate.convertAndSend(
                RabbitMQConstant.ORDER_EXCHANGE,
                RabbitMQConstant.ORDER_NOTIFY_ROUTING_KEY,
                message
        );
    }
}

