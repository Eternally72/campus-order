package com.rabbit.user.mapper;

import com.rabbit.common.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收藏Mapper
 */
@Mapper
public interface FavoriteMapper {

    /**
     * 插入收藏记录
     */
    int insert(Favorite favorite);

    /**
     * 根据用户ID和商品ID删除收藏
     */
    int deleteByUserIdAndGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);

    /**
     * 根据ID删除
     */
    int deleteById(@Param("id") Long id);

    /**
     * 检查是否已收藏
     */
    int countByUserIdAndGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);

    /**
     * 查询用户收藏列表
     */
    List<Favorite> selectByUserId(@Param("userId") Long userId);

    /**
     * 统计用户收藏数量
     */
    long countByUserId(@Param("userId") Long userId);
}
