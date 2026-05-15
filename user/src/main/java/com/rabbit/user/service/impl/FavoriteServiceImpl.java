package com.rabbit.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rabbit.common.entity.Favorite;
import com.rabbit.common.exception.BusinessException;
import com.rabbit.common.feign.GoodsFeignClient;
import com.rabbit.common.result.Result;
import com.rabbit.common.utils.IdUtil;
import com.rabbit.common.vo.PageVO;
import com.rabbit.user.mapper.FavoriteMapper;
import com.rabbit.user.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 收藏服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final GoodsFeignClient goodsFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(Long userId, Long goodsId) {
        // 检查是否已收藏
        if (isFavorite(userId, goodsId)) {
            throw new BusinessException("已经收藏过该商品");
        }

        Favorite favorite = new Favorite();
        favorite.setId(IdUtil.nextId());
        favorite.setUserId(userId);
        favorite.setGoodsId(goodsId);
        favorite.setCreateTime(LocalDateTime.now());
        favorite.setUpdateTime(LocalDateTime.now());
        favorite.setDeleted(0);
        favoriteMapper.insert(favorite);

        syncFavoriteCount(goodsId, true);
        log.info("添加收藏成功: userId={}, goodsId={}", userId, goodsId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(Long userId, Long goodsId) {
        favoriteMapper.deleteByUserIdAndGoodsId(userId, goodsId);

        syncFavoriteCount(goodsId, false);
        log.info("取消收藏成功: userId={}, goodsId={}", userId, goodsId);
    }

    @Override
    public boolean isFavorite(Long userId, Long goodsId) {
        return favoriteMapper.countByUserIdAndGoodsId(userId, goodsId) > 0;
    }

    @Override
    public PageVO<Favorite> getMyFavorites(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Favorite> list = favoriteMapper.selectByUserId(userId);
        PageInfo<Favorite> pageInfo = new PageInfo<>(list);
        return PageVO.of(pageInfo.getTotal(), pageNum, pageSize, list);
    }

    private void syncFavoriteCount(Long goodsId, boolean increase) {
        Result<Boolean> result = increase
                ? goodsFeignClient.increaseFavoriteCount(goodsId)
                : goodsFeignClient.decreaseFavoriteCount(goodsId);
        if (result == null || !Boolean.TRUE.equals(result.getData())) {
            throw new BusinessException("同步商品收藏数失败");
        }
    }
}
