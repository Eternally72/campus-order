package com.rabbit.common.feign;

import com.rabbit.common.result.Result;
import com.rabbit.common.vo.GoodsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品服务Feign客户端
 */
@FeignClient(name = "goods-service", fallbackFactory = GoodsFeignClientFallback.class)
public interface GoodsFeignClient {

    /**
     * 获取商品详情
     */
    @GetMapping("/goods/detail/{id}")
    Result<GoodsVO> getGoodsDetail(@PathVariable("id") Long id);

    /**
     * 扣减库存
     */
    @PostMapping("/goods/deductStock")
    Result<Boolean> deductStock(@RequestParam("goodsId") Long goodsId,
                                 @RequestParam("quantity") Integer quantity);

    /**
     * 恢复库存
     */
    @PostMapping("/goods/restoreStock")
    Result<Boolean> restoreStock(@RequestParam("goodsId") Long goodsId,
                                  @RequestParam("quantity") Integer quantity);

    /**
     * 增加收藏数
     */
    @PostMapping("/goods/favorite/increase")
    Result<Boolean> increaseFavoriteCount(@RequestParam("goodsId") Long goodsId);

    /**
     * 减少收藏数
     */
    @PostMapping("/goods/favorite/decrease")
    Result<Boolean> decreaseFavoriteCount(@RequestParam("goodsId") Long goodsId);
}

