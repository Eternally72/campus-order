package com.rabbit.goods.service.impl;

import com.rabbit.common.entity.Category;
import com.rabbit.goods.mapper.CategoryMapper;
import com.rabbit.goods.service.CategoryService;
import com.rabbit.common.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分类服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> getCategoryTree() {
        List<Category> categories = categoryMapper.selectAllEnabled();
        List<CategoryVO> voList = categories.stream().map(category -> {
            CategoryVO vo = new CategoryVO();
            BeanUtils.copyProperties(category, vo);
            return vo;
        }).collect(Collectors.toList());
        return buildTree(voList);
    }

    @Override
    public List<CategoryVO> getAllCategories() {
        List<Category> categories = categoryMapper.selectAllEnabled();
        return categories.stream().map(category -> {
            CategoryVO vo = new CategoryVO();
            BeanUtils.copyProperties(category, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 构建分类树
     */
    private List<CategoryVO> buildTree(List<CategoryVO> voList) {
        Map<Long, List<CategoryVO>> parentIdMap = voList.stream()
                .collect(Collectors.groupingBy(CategoryVO::getParentId));
        voList.forEach(vo -> vo.setChildren(parentIdMap.getOrDefault(vo.getId(), new ArrayList<>())));
        return voList.stream()
                .filter(vo -> vo.getParentId() == 0)
                .collect(Collectors.toList());
    }
}

