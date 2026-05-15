package com.rabbit.notify.service.impl;

import com.rabbit.common.entity.NotifyRecord;
import com.rabbit.notify.mapper.NotifyRecordMapper;
import com.rabbit.notify.service.NotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 通知服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements NotifyService {

    private final NotifyRecordMapper notifyRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendOrderCreatedNotify(Long orderId, String orderNo, Long buyerId, Long sellerId) {
        NotifyRecord sellerNotify = createNotifyRecord(
                sellerId, 1, "您有新的订单",
                "买家下单了，订单编号：" + orderNo + "，请及时处理",
                orderId, "ORDER"
        );
        notifyRecordMapper.insert(sellerNotify);
        log.info("发送订单创建通知: orderId={}, sellerId={}", orderId, sellerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendOrderPaidNotify(Long orderId, String orderNo, Long sellerId) {
        NotifyRecord sellerNotify = createNotifyRecord(
                sellerId, 1, "订单已支付",
                "订单编号：" + orderNo + " 已完成支付，请尽快发货",
                orderId, "ORDER"
        );
        notifyRecordMapper.insert(sellerNotify);
        log.info("发送订单支付通知: orderId={}, sellerId={}", orderId, sellerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendOrderCancelledNotify(Long orderId, String orderNo, Long buyerId, Long sellerId) {
        NotifyRecord sellerNotify = createNotifyRecord(
                sellerId, 1, "订单已取消",
                "订单编号：" + orderNo + " 已被买家取消",
                orderId, "ORDER"
        );
        notifyRecordMapper.insert(sellerNotify);
        log.info("发送订单取消通知: orderId={}, sellerId={}", orderId, sellerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendOrderCompletedNotify(Long orderId, String orderNo, Long buyerId, Long sellerId) {
        NotifyRecord sellerNotify = createNotifyRecord(
                sellerId, 1, "订单已完成",
                "订单编号：" + orderNo + " 已确认收货，交易完成",
                orderId, "ORDER"
        );
        notifyRecordMapper.insert(sellerNotify);

        NotifyRecord buyerNotify = createNotifyRecord(
                buyerId, 1, "交易完成",
                "订单编号：" + orderNo + " 交易已完成，感谢您的购买",
                orderId, "ORDER"
        );
        notifyRecordMapper.insert(buyerNotify);
        log.info("发送订单完成通知: orderId={}, buyerId={}, sellerId={}", orderId, buyerId, sellerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long notifyId) {
        notifyRecordMapper.markAsRead(notifyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(Long userId) {
        notifyRecordMapper.markAllAsRead(userId);
    }

    @Override
    public long countUnread(Long userId) {
        return notifyRecordMapper.countUnread(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        notifyRecordMapper.deleteById(id);
    }

    private NotifyRecord createNotifyRecord(Long userId, Integer type, String title,
                                             String content, Long bizId, String bizType) {
        NotifyRecord record = new NotifyRecord();
        record.setId(generateId());
        record.setUserId(userId);
        record.setType(type);
        record.setTitle(title);
        record.setContent(content);
        record.setBizId(bizId);
        record.setBizType(bizType);
        record.setReadFlag(0);
        record.setSendStatus(1);
        record.setSendTime(LocalDateTime.now());
        record.setDeleted(0);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        return record;
    }

    private Long generateId() {
        return System.currentTimeMillis() * 1000000 + (long)(Math.random() * 1000000);
    }
}

