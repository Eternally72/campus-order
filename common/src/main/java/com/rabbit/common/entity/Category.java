package com.rabbit.common.entity;

import com.rabbit.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类ID（0表示顶级分类）
     */
    private Long parentId;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;
}

