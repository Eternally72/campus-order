package com.rabbit.notify.listener;

import com.rabbit.common.constant.RabbitMQConstant;
import com.rabbit.notify.service.NotifyService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 订单消息监听器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMessageListener {

    private final NotifyService notifyService;

    /**
     * 监听订单通知队列
     */
    @RabbitListener(queues = RabbitMQConstant.ORDER_NOTIFY_QUEUE)
    public void handleOrderMessage(Map<String, Object> message, Message amqpMessage, Channel channel) {
        long deliveryTag = amqpMessage.getMessageProperties().getDeliveryTag();

        try {
            log.info("收到订单消息: {}", message);

            String type = (String) message.get("type");
            Long orderId = Long.valueOf(message.get("orderId").toString());
            String orderNo = (String) message.get("orderNo");

            Long buyerId = Long.valueOf(message.get("buyerId").toString());
            Long sellerId = Long.valueOf(message.get("sellerId").toString());

            switch (type) {
                case "ORDER_CREATED":
                    notifyService.sendOrderCreatedNotify(orderId, orderNo, buyerId, sellerId);
                    break;
                case "ORDER_PAID":
                    notifyService.sendOrderPaidNotify(orderId, orderNo, sellerId);
                    break;
                case "ORDER_CANCELLED":
                    notifyService.sendOrderCancelledNotify(orderId, orderNo, buyerId, sellerId);
                    break;
                case "ORDER_COMPLETED":
                    notifyService.sendOrderCompletedNotify(orderId, orderNo, buyerId, sellerId);
                    break;
                default:
                    log.warn("未知的消息类型: {}", type);
            }

            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            log.info("消息处理成功: orderId={}, type={}", orderId, type);

        } catch (Exception e) {
            log.error("处理订单消息失败", e);
            try {
                // 拒绝消息，不重新入队（会进入死信队列）
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                log.error("拒绝消息失败", ex);
            }
        }
    }

    /**
     * 监听死信队列
     */
    @RabbitListener(queues = RabbitMQConstant.DEAD_LETTER_QUEUE)
    public void handleDeadLetterMessage(Map<String, Object> message, Message amqpMessage, Channel channel) {
        long deliveryTag = amqpMessage.getMessageProperties().getDeliveryTag();

        try {
            log.warn("收到死信消息: {}", message);

            log.warn("死信消息等待人工处理或告警接入: {}", message);

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("处理死信消息失败", e);
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                log.error("拒绝死信消息失败", ex);
            }
        }
    }
}

