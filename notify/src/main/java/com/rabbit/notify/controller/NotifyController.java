package com.rabbit.notify.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rabbit.common.result.Result;
import com.rabbit.common.vo.PageVO;
import com.rabbit.common.entity.NotifyRecord;
import com.rabbit.notify.mapper.NotifyRecordMapper;
import com.rabbit.notify.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;
    private final NotifyRecordMapper notifyRecordMapper;

    /**
     * 获取我的通知列表
     */
    @GetMapping("/my")
    public Result<PageVO<NotifyRecord>> getMyNotifications(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) Integer readFlag,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<NotifyRecord> list = notifyRecordMapper.selectByUserId(userId, readFlag);
        PageInfo<NotifyRecord> pageInfo = new PageInfo<>(list);

        PageVO<NotifyRecord> pageVO = PageVO.of(pageInfo.getTotal(), pageNum, pageSize, list);
        return Result.success(pageVO);
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread/count")
    public Result<Long> getUnreadCount(@RequestHeader("X-User-Id") Long userId) {
        long count = notifyService.countUnread(userId);
        return Result.success(count);
    }

    /**
     * 标记通知为已读
     */
    @PostMapping("/read/{id}")
    public Result<Void> markAsRead(@PathVariable Long id) {
        notifyService.markAsRead(id);
        return Result.success();
    }

    /**
     * 标记所有通知为已读
     */
    @PostMapping("/read/all")
    public Result<Void> markAllAsRead(@RequestHeader("X-User-Id") Long userId) {
        notifyService.markAllAsRead(userId);
        return Result.success();
    }

    /**
     * 删除通知
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteNotify(@PathVariable Long id) {
        notifyService.deleteById(id);
        return Result.success();
    }
}

