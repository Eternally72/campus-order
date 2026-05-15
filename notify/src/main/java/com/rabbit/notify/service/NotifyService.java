package com.rabbit.notify.service;

/**
 * 通知服务接口
 */
public interface NotifyService {

    /**
     * 发送订单创建通知
     */
    void sendOrderCreatedNotify(Long orderId, String orderNo, Long buyerId, Long sellerId);

    /**
     * 发送订单支付通知
     */
    void sendOrderPaidNotify(Long orderId, String orderNo, Long sellerId);

    /**
     * 发送订单取消通知
     */
    void sendOrderCancelledNotify(Long orderId, String orderNo, Long buyerId, Long sellerId);

    /**
     * 发送订单完成通知
     */
    void sendOrderCompletedNotify(Long orderId, String orderNo, Long buyerId, Long sellerId);

    /**
     * 标记通知已读
     */
    void markAsRead(Long notifyId);

    /**
     * 标记用户所有通知已读
     */
    void markAllAsRead(Long userId);

    /**
     * 统计未读数
     */
    long countUnread(Long userId);

    /**
     * 删除通知
     */
    void deleteById(Long id);
}

