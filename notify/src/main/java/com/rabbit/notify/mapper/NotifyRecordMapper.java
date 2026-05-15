package com.rabbit.notify.mapper;

import com.rabbit.common.entity.NotifyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知记录Mapper
 */
@Mapper
public interface NotifyRecordMapper {

    /**
     * 根据ID查询
     */
    NotifyRecord selectById(@Param("id") Long id);

    /**
     * 插入通知
     */
    int insert(NotifyRecord record);

    /**
     * 更新通知
     */
    int update(NotifyRecord record);

    /**
     * 删除通知
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询用户通知列表
     */
    List<NotifyRecord> selectByUserId(@Param("userId") Long userId, @Param("readFlag") Integer readFlag);

    /**
     * 统计用户未读通知数
     */
    long countUnread(@Param("userId") Long userId);

    /**
     * 标记已读
     */
    int markAsRead(@Param("id") Long id);

    /**
     * 标记用户所有通知已读
     */
    int markAllAsRead(@Param("userId") Long userId);
}

