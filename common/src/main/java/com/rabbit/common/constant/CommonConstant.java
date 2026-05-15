package com.rabbit.common.constant;

/**
 * 公共常量
 */
public class CommonConstant {

    private CommonConstant() {
    }

    /**
     * Token请求头名称
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 用户ID请求头名称
     */
    public static final String USER_ID_HEADER = "X-User-Id";

    /**
     * 用户名请求头名称
     */
    public static final String USERNAME_HEADER = "X-Username";

    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大分页大小
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 删除标记：未删除
     */
    public static final int NOT_DELETED = 0;

    /**
     * 删除标记：已删除
     */
    public static final int DELETED = 1;

    /**
     * 启用状态
     */
    public static final int STATUS_ENABLE = 1;

    /**
     * 禁用状态
     */
    public static final int STATUS_DISABLE = 0;
}

