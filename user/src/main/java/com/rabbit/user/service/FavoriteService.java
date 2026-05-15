package com.rabbit.user.service;

import com.rabbit.common.entity.Favorite;
import com.rabbit.common.vo.PageVO;

/**
 * 收藏服务接口
 */
public interface FavoriteService {

    /**
     * 添加收藏
     */
    void addFavorite(Long userId, Long goodsId);

    /**
     * 取消收藏
     */
    void removeFavorite(Long userId, Long goodsId);

    /**
     * 检查是否已收藏
     */
    boolean isFavorite(Long userId, Long goodsId);

    /**
     * 获取我的收藏列表
     */
    PageVO<Favorite> getMyFavorites(Long userId, Integer pageNum, Integer pageSize);
}
