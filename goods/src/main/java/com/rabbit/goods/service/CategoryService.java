package com.rabbit.goods.service;

import com.rabbit.common.vo.CategoryVO;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {

    /**
     * 获取分类树
     */
    List<CategoryVO> getCategoryTree();

    /**
     * 获取所有分类列表
     */
    List<CategoryVO> getAllCategories();
}

