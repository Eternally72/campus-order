package com.rabbit.common.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页响应VO
 *
 * @param <T> 数据类型
 */
@Data
public class PageVO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 数据列表
     */
    private List<T> list;

    public PageVO() {
    }

    public PageVO(Long total, Integer pageNum, Integer pageSize, List<T> list) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = (int) Math.ceil((double) total / pageSize);
        this.list = list;
    }

    /**
     * 构建分页响应
     */
    public static <T> PageVO<T> of(Long total, Integer pageNum, Integer pageSize, List<T> list) {
        return new PageVO<>(total, pageNum, pageSize, list);
    }

    /**
     * 空分页响应
     */
    public static <T> PageVO<T> empty(Integer pageNum, Integer pageSize) {
        return new PageVO<>(0L, pageNum, pageSize, List.of());
    }
}

