package com.rabbit.common.feign;

import com.rabbit.common.result.Result;
import com.rabbit.common.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 商品服务Feign降级工厂
 */
@Slf4j
@Component
public class GoodsFeignClientFallback implements FallbackFactory<GoodsFeignClient> {

    @Override
    public GoodsFeignClient create(Throwable cause) {
        log.error("商品服务调用失败", cause);
        return new GoodsFeignClient() {
            @Override
            public Result<GoodsVO> getGoodsDetail(Long id) {
                return Result.fail("商品服务不可用");
            }

            @Override
            public Result<Boolean> deductStock(Long goodsId, Integer quantity) {
                return Result.fail("商品服务不可用");
            }

            @Override
            public Result<Boolean> restoreStock(Long goodsId, Integer quantity) {
                return Result.fail("商品服务不可用");
            }

            @Override
            public Result<Boolean> increaseFavoriteCount(Long goodsId) {
                return Result.fail("商品服务不可用");
            }

            @Override
            public Result<Boolean> decreaseFavoriteCount(Long goodsId) {
                return Result.fail("商品服务不可用");
            }
        };
    }
}

