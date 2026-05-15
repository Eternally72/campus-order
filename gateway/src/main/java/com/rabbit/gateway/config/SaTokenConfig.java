package com.rabbit.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token配置（响应式）
 */
@Configuration
public class SaTokenConfig {

    /**
     * 注册Sa-Token全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截所有路径
                .addInclude("/**")
                // 放行登录、注册等路径
                .addExclude(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/auth/captcha",
                        "/api/goods/list",
                        "/api/goods/detail/**",
                        "/api/goods/category/**",
                        "/favicon.ico",
                        "/error"
                )
                // 鉴权方法
                .setAuth(obj -> {
                    // 登录校验
                    SaRouter.match("/**", r -> StpUtil.checkLogin());

                    // SaRouter.match("/api/admin/**", r -> StpUtil.checkRole("admin"));
                    // SaRouter.match("/api/user/delete", r -> StpUtil.checkPermission("user:delete"));
                })
                // 异常处理
                .setError(e -> "{\"code\":401,\"message\":\"" + e.getMessage() + "\",\"data\":null}");
    }
}

