package com.rabbit.user.controller;

import com.rabbit.common.entity.Favorite;
import com.rabbit.common.result.Result;
import com.rabbit.common.vo.PageVO;
import com.rabbit.user.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/user/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 添加收藏
     */
    @PostMapping("/{goodsId}")
    public Result<Void> addFavorite(@RequestHeader("X-User-Id") Long userId,
                                     @PathVariable Long goodsId) {
        favoriteService.addFavorite(userId, goodsId);
        return Result.success();
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/{goodsId}")
    public Result<Void> removeFavorite(@RequestHeader("X-User-Id") Long userId,
                                        @PathVariable Long goodsId) {
        favoriteService.removeFavorite(userId, goodsId);
        return Result.success();
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/check/{goodsId}")
    public Result<Boolean> isFavorite(@RequestHeader("X-User-Id") Long userId,
                                       @PathVariable Long goodsId) {
        boolean result = favoriteService.isFavorite(userId, goodsId);
        return Result.success(result);
    }

    /**
     * 获取我的收藏列表
     */
    @GetMapping("/my")
    public Result<PageVO<Favorite>> getMyFavorites(@RequestHeader("X-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<Favorite> pageVO = favoriteService.getMyFavorites(userId, pageNum, pageSize);
        return Result.success(pageVO);
    }
}

