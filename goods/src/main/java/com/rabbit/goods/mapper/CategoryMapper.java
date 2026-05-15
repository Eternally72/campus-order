package com.rabbit.goods.mapper;

import com.rabbit.common.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类Mapper
 */
@Mapper
public interface CategoryMapper {

    /**
     * 根据ID查询分类
     */
    Category selectById(@Param("id") Long id);

    /**
     * 查询所有启用的分类
     */
    List<Category> selectAllEnabled();

    /**
     * 插入分类
     */
    int insert(@Param("category") Category category);

    /**
     * 更新分类
     */
    int update(@Param("category") Category category);

    /**
     * 删除分类
     */
    int deleteById(@Param("id") Long id);
}

