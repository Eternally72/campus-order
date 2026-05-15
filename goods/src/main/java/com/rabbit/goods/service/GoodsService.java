package com.rabbit.goods.service;

import com.rabbit.common.vo.PageVO;
import com.rabbit.common.dto.GoodsDTO;
import com.rabbit.common.dto.GoodsQueryDTO;
import com.rabbit.common.vo.GoodsVO;

/**
 * 商品服务接口
 */
public interface GoodsService {

    /**
     * 分页查询商品
     */
    PageVO<GoodsVO> pageGoods(GoodsQueryDTO queryDTO);

    /**
     * 获取商品详情
     */
    GoodsVO getGoodsDetail(Long id);

    /**
     * 创建商品
     */
    Long createGoods(GoodsDTO goodsDTO);

    /**
     * 更新商品
     */
    void updateGoods(GoodsDTO goodsDTO);

    /**
     * 上架商品
     */
    void onShelf(Long id);

    /**
     * 下架商品
     */
    void offShelf(Long id);

    /**
     * 扣减库存
     */
    boolean deductStock(Long goodsId, Integer quantity);

    /**
     * 恢复库存
     */
    boolean restoreStock(Long goodsId, Integer quantity);

    /**
     * 增加收藏数
     */
    boolean increaseFavoriteCount(Long goodsId);

    /**
     * 减少收藏数
     */
    boolean decreaseFavoriteCount(Long goodsId);

    /**
     * 删除商品
     */
    void deleteById(Long id);
}

