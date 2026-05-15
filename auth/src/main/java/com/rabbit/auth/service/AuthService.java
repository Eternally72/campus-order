package com.rabbit.auth.service;

import com.rabbit.common.dto.LoginDTO;
import com.rabbit.common.dto.RegisterDTO;
import com.rabbit.common.vo.LoginVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录参数
     * @return 登录信息
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户注册
     *
     * @param registerDTO 注册参数
     */
    void register(RegisterDTO registerDTO);
}

