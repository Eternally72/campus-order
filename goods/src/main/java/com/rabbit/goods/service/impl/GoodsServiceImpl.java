package com.rabbit.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rabbit.common.exception.BusinessException;
import com.rabbit.common.result.ResultCodeEnum;
import com.rabbit.common.utils.IdUtil;
import com.rabbit.common.vo.PageVO;
import com.rabbit.common.dto.GoodsDTO;
import com.rabbit.common.dto.GoodsQueryDTO;
import com.rabbit.common.entity.Goods;
import com.rabbit.goods.mapper.GoodsMapper;
import com.rabbit.goods.service.GoodsService;
import com.rabbit.common.vo.GoodsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;

    @Override
    public PageVO<GoodsVO> pageGoods(GoodsQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        List<Goods> goodsList = goodsMapper.selectList(
                queryDTO.getKeyword(),
                queryDTO.getCategoryId(),
                queryDTO.getSellerId(),
                queryDTO.getStatus()
        );

        PageInfo<Goods> pageInfo = new PageInfo<>(goodsList);

        List<GoodsVO> voList = goodsList.stream().map(goods -> {
            GoodsVO vo = new GoodsVO();
            BeanUtils.copyProperties(goods, vo);
            return vo;
        }).collect(Collectors.toList());

        return PageVO.of(pageInfo.getTotal(), queryDTO.getPageNum(), queryDTO.getPageSize(), voList);
    }

    @Override
    public GoodsVO getGoodsDetail(Long id) {
        Goods goods = goodsMapper.selectById(id);
        if (goods == null) {
            throw new BusinessException(ResultCodeEnum.GOODS_NOT_EXIST);
        }

        // 增加浏览量
        goodsMapper.incrementViewCount(id);

        GoodsVO vo = new GoodsVO();
        BeanUtils.copyProperties(goods, vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createGoods(GoodsDTO goodsDTO) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsDTO, goods);

        // 生成ID
        goods.setId(IdUtil.nextId());
        goods.setStatus(1); // 默认在售
        goods.setViewCount(0);
        goods.setFavoriteCount(0);
        goods.setDeleted(0);
        goods.setCreateTime(LocalDateTime.now());
        goods.setUpdateTime(LocalDateTime.now());

        goodsMapper.insert(goods);

        log.info("创建商品成功: goodsId={}, title={}", goods.getId(), goods.getTitle());
        return goods.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGoods(GoodsDTO goodsDTO) {
        Goods goods = goodsMapper.selectById(goodsDTO.getId());
        if (goods == null) {
            throw new BusinessException(ResultCodeEnum.GOODS_NOT_EXIST);
        }
        BeanUtils.copyProperties(goodsDTO, goods);
        goodsMapper.update(goods);
        log.info("更新商品成功: goodsId={}", goods.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onShelf(Long id) {
        Goods goods = goodsMapper.selectById(id);
        if (goods == null) {
            throw new BusinessException(ResultCodeEnum.GOODS_NOT_EXIST);
        }
        goods.setStatus(1);
        goodsMapper.update(goods);
        log.info("商品上架成功: goodsId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void offShelf(Long id) {
        Goods goods = goodsMapper.selectById(id);
        if (goods == null) {
            throw new BusinessException(ResultCodeEnum.GOODS_NOT_EXIST);
        }
        goods.setStatus(0);
        goodsMapper.update(goods);
        log.info("商品下架成功: goodsId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deductStock(Long goodsId, Integer quantity) {
        int rows = goodsMapper.deductStock(goodsId, quantity);
        if (rows == 0) {
            throw new BusinessException(ResultCodeEnum.GOODS_STOCK_NOT_ENOUGH);
        }
        log.info("扣减库存成功: goodsId={}, quantity={}", goodsId, quantity);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean restoreStock(Long goodsId, Integer quantity) {
        int rows = goodsMapper.restoreStock(goodsId, quantity);
        log.info("恢复库存成功: goodsId={}, quantity={}", goodsId, quantity);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseFavoriteCount(Long goodsId) {
        return goodsMapper.incrementFavoriteCount(goodsId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseFavoriteCount(Long goodsId) {
        return goodsMapper.decrementFavoriteCount(goodsId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        goodsMapper.deleteById(id);
        log.info("删除商品成功: goodsId={}", id);
    }
}

