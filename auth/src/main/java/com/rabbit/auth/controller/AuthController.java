package com.rabbit.auth.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.rabbit.common.dto.LoginDTO;
import com.rabbit.common.dto.RegisterDTO;
import com.rabbit.auth.service.AuthService;
import com.rabbit.common.utils.RedisUtil;
import com.rabbit.common.vo.CaptchaVO;
import com.rabbit.common.vo.LoginVO;
import com.rabbit.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RedisUtil redisUtil;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return Result.success(loginVO);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return Result.success();
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success();
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result<SaTokenInfo> getTokenInfo() {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return Result.success(tokenInfo);
    }

    /**
     * 检查是否登录
     */
    @GetMapping("/check")
    public Result<Boolean> checkLogin() {
        return Result.success(StpUtil.isLogin());
    }

    @GetMapping("/captcha")
    public Result<CaptchaVO> getCaptcha() {
        String captchaKey = UUID.randomUUID().toString().replace("-", "");
        String captcha = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
        redisUtil.set("auth:captcha:" + captchaKey, captcha, 5, TimeUnit.MINUTES);

        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaKey(captchaKey);
        captchaVO.setCaptcha(captcha);
        return Result.success(captchaVO);
    }
}

