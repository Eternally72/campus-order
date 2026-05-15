package com.rabbit.common.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录响应VO
 */
@Data
public class LoginVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * Token
     */
    private String token;

    /**
     * Token名称
     */
    private String tokenName;

    /**
     * Token有效期（秒）
     */
    private Long tokenTimeout;
}

