package com.rabbit.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.rabbit.common.dto.LoginDTO;
import com.rabbit.common.dto.RegisterDTO;
import com.rabbit.common.dto.UserDTO;
import com.rabbit.auth.service.AuthService;
import com.rabbit.common.feign.UserFeignClient;
import com.rabbit.common.result.Result;
import com.rabbit.common.utils.RedisUtil;
import com.rabbit.common.vo.LoginVO;
import com.rabbit.common.exception.BusinessException;
import com.rabbit.common.result.ResultCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserFeignClient userFeignClient;
    private final RedisUtil redisUtil;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        validateCaptcha(loginDTO.getCaptchaKey(), loginDTO.getCaptcha());

        Long userId = validateUser(loginDTO.getUsername(), loginDTO.getPassword());

        if (userId == null) {
            throw new BusinessException(ResultCodeEnum.USER_PASSWORD_ERROR);
        }

        // Sa-Token登录
        StpUtil.login(userId);

        // 构建返回信息
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(userId);
        loginVO.setUsername(loginDTO.getUsername());
        loginVO.setToken(StpUtil.getTokenValue());
        loginVO.setTokenName(StpUtil.getTokenName());
        loginVO.setTokenTimeout(StpUtil.getTokenTimeout());

        log.info("用户登录成功: userId={}, username={}", userId, loginDTO.getUsername());
        return loginVO;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        // 校验两次密码是否一致
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(registerDTO.getUsername());
        userDTO.setPassword(registerDTO.getPassword());
        userDTO.setNickname(registerDTO.getUsername());
        userDTO.setPhone(registerDTO.getPhone());
        userDTO.setEmail(registerDTO.getEmail());
        userDTO.setStatus(1);

        Result<Long> result = userFeignClient.createUser(userDTO);
        if (result == null || !ResultCodeEnum.SUCCESS.getCode().equals(result.getCode()) || result.getData() == null) {
            String message = result == null ? "用户服务不可用" : result.getMessage();
            throw new BusinessException(ResultCodeEnum.USER_ALREADY_EXIST, message);
        }

        log.info("用户注册成功: userId={}, username={}", result.getData(), registerDTO.getUsername());
    }

    /**
     * 通过用户服务验证用户身份。
     */
    private Long validateUser(String username, String password) {
        Result<Long> result = userFeignClient.validatePassword(username, password);
        if (result == null) {
            throw new BusinessException(ResultCodeEnum.SERVICE_UNAVAILABLE, "用户服务不可用");
        }
        if (!ResultCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
            throw new BusinessException(result.getCode(), result.getMessage());
        }
        return result.getData();
    }

    private void validateCaptcha(String captchaKey, String captcha) {
        if (!StringUtils.hasText(captchaKey) && !StringUtils.hasText(captcha)) {
            return;
        }
        if (!StringUtils.hasText(captchaKey) || !StringUtils.hasText(captcha)) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR, "验证码参数不完整");
        }

        String redisKey = "auth:captcha:" + captchaKey;
        String cachedCaptcha = redisUtil.get(redisKey, String.class);
        if (cachedCaptcha == null || !cachedCaptcha.equalsIgnoreCase(captcha)) {
            throw new BusinessException(ResultCodeEnum.LOGIN_FAIL, "验证码错误或已过期");
        }
        redisUtil.delete(redisKey);
    }
}

