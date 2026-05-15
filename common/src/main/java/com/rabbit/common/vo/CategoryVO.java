package com.rabbit.common.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分类VO
 */
@Data
public class CategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Long parentId;

    private String icon;

    private Integer sort;

    private Integer status;

    /**
     * 子分类
     */
    private List<CategoryVO> children;
}

