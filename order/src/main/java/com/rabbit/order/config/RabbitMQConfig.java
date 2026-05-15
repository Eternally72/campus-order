package com.rabbit.order.config;

import com.rabbit.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 消息转换器
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate配置
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * 订单交换机
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(RabbitMQConstant.ORDER_EXCHANGE, true, false);
    }

    /**
     * 订单通知队列
     */
    @Bean
    public Queue orderNotifyQueue() {
        return QueueBuilder.durable(RabbitMQConstant.ORDER_NOTIFY_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstant.DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstant.DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定订单通知队列到交换机
     */
    @Bean
    public Binding orderNotifyBinding() {
        return BindingBuilder
                .bind(orderNotifyQueue())
                .to(orderExchange())
                .with(RabbitMQConstant.ORDER_NOTIFY_ROUTING_KEY);
    }

    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(RabbitMQConstant.DEAD_LETTER_EXCHANGE, true, false);
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(RabbitMQConstant.DEAD_LETTER_QUEUE).build();
    }

    /**
     * 绑定死信队列到死信交换机
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(RabbitMQConstant.DEAD_LETTER_ROUTING_KEY);
    }
}

