package com.rabbit.common.constant;

/**
 * RabbitMQ常量
 */
public class RabbitMQConstant {

    private RabbitMQConstant() {
    }

    /**
     * 订单交换机
     */
    public static final String ORDER_EXCHANGE = "order.exchange";

    /**
     * 订单队列
     */
    public static final String ORDER_QUEUE = "order.queue";

    /**
     * 订单路由键
     */
    public static final String ORDER_ROUTING_KEY = "order.routing.key";

    /**
     * 订单通知队列
     */
    public static final String ORDER_NOTIFY_QUEUE = "order.notify.queue";

    /**
     * 订单通知路由键
     */
    public static final String ORDER_NOTIFY_ROUTING_KEY = "order.notify.routing.key";

    /**
     * 死信交换机
     */
    public static final String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";

    /**
     * 死信队列
     */
    public static final String DEAD_LETTER_QUEUE = "dead.letter.queue";

    /**
     * 死信路由键
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "dead.letter.routing.key";
}

