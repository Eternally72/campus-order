package com.rabbit.goods.controller;

import com.rabbit.common.result.Result;
import com.rabbit.common.vo.PageVO;
import com.rabbit.common.dto.GoodsDTO;
import com.rabbit.common.dto.GoodsQueryDTO;
import com.rabbit.goods.service.GoodsService;
import com.rabbit.common.vo.GoodsVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    /**
     * 分页查询商品列表
     */
    @GetMapping("/list")
    public Result<PageVO<GoodsVO>> list(GoodsQueryDTO queryDTO) {
        PageVO<GoodsVO> pageVO = goodsService.pageGoods(queryDTO);
        return Result.success(pageVO);
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/detail/{id}")
    public Result<GoodsVO> detail(@PathVariable Long id) {
        GoodsVO goodsVO = goodsService.getGoodsDetail(id);
        return Result.success(goodsVO);
    }

    /**
     * 创建商品
     */
    @PostMapping
    public Result<Long> create(@Valid @RequestBody GoodsDTO goodsDTO) {
        Long goodsId = goodsService.createGoods(goodsDTO);
        return Result.success(goodsId);
    }

    /**
     * 更新商品
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody GoodsDTO goodsDTO) {
        goodsService.updateGoods(goodsDTO);
        return Result.success();
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        goodsService.deleteById(id);
        return Result.success();
    }

    /**
     * 上架商品
     */
    @PostMapping("/onShelf/{id}")
    public Result<Void> onShelf(@PathVariable Long id) {
        goodsService.onShelf(id);
        return Result.success();
    }

    /**
     * 下架商品
     */
    @PostMapping("/offShelf/{id}")
    public Result<Void> offShelf(@PathVariable Long id) {
        goodsService.offShelf(id);
        return Result.success();
    }

    /**
     * 扣减库存（供内部调用）
     */
    @PostMapping("/deductStock")
    public Result<Boolean> deductStock(@RequestParam Long goodsId, @RequestParam Integer quantity) {
        boolean result = goodsService.deductStock(goodsId, quantity);
        return Result.success(result);
    }

    /**
     * 恢复库存（供内部调用）
     */
    @PostMapping("/restoreStock")
    public Result<Boolean> restoreStock(@RequestParam Long goodsId, @RequestParam Integer quantity) {
        boolean result = goodsService.restoreStock(goodsId, quantity);
        return Result.success(result);
    }

    /**
     * 增加收藏数（供内部调用）
     */
    @PostMapping("/favorite/increase")
    public Result<Boolean> increaseFavoriteCount(@RequestParam Long goodsId) {
        boolean result = goodsService.increaseFavoriteCount(goodsId);
        return Result.success(result);
    }

    /**
     * 减少收藏数（供内部调用）
     */
    @PostMapping("/favorite/decrease")
    public Result<Boolean> decreaseFavoriteCount(@RequestParam Long goodsId) {
        boolean result = goodsService.decreaseFavoriteCount(goodsId);
        return Result.success(result);
    }
}

