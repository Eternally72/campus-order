package com.rabbit.goods.mapper;

import com.rabbit.common.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品Mapper
 */
@Mapper
public interface GoodsMapper {

    /**
     * 根据ID查询商品
     */
    Goods selectById(@Param("id") Long id);

    /**
     * 插入商品
     */
    int insert(@Param("goods") Goods goods);

    /**
     * 更新商品
     */
    int update(@Param("goods") Goods goods);

    /**
     * 删除商品（逻辑删除）
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询商品列表
     */
    List<Goods> selectList(@Param("keyword") String keyword,
                           @Param("categoryId") Long categoryId,
                           @Param("sellerId") Long sellerId,
                           @Param("status") Integer status);

    /**
     * 扣减库存
     */
    int deductStock(@Param("goodsId") Long goodsId, @Param("quantity") Integer quantity);

    /**
     * 恢复库存
     */
    int restoreStock(@Param("goodsId") Long goodsId, @Param("quantity") Integer quantity);

    /**
     * 增加浏览量
     */
    int incrementViewCount(@Param("id") Long id);

    /**
     * 增加收藏数
     */
    int incrementFavoriteCount(@Param("goodsId") Long goodsId);

    /**
     * 减少收藏数
     */
    int decrementFavoriteCount(@Param("goodsId") Long goodsId);
}

