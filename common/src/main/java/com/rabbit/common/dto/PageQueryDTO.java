package com.rabbit.common.dto;

import com.rabbit.common.constant.CommonConstant;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询请求DTO
 */
@Data
public class PageQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer pageNum = CommonConstant.DEFAULT_PAGE_NUM;

    /**
     * 每页大小
     */
    private Integer pageSize = CommonConstant.DEFAULT_PAGE_SIZE;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 是否升序（true-升序，false-降序）
     */
    private Boolean asc = true;

    /**
     * 获取分页偏移量
     */
    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }
}

