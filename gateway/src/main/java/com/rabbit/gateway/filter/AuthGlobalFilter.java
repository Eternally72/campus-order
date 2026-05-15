package com.rabbit.gateway.filter;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 全局鉴权过滤器
 * 透传用户信息到下游服务
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 白名单路径（无需登录）
     */
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/captcha",
            "/api/goods/list",
            "/api/goods/detail/**",
            "/api/goods/category/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.debug("请求路径: {}", path);

        // 白名单路径直接放行
        if (isWhitePath(path)) {
            return chain.filter(exchange);
        }

        // 检查是否登录
        try {
            // 获取登录用户ID
            Object loginId = StpUtil.getLoginId();

            // 将用户ID透传到下游服务
            ServerHttpRequest newRequest = request.mutate()
                    .header("X-User-Id", String.valueOf(loginId))
                    .build();

            return chain.filter(exchange.mutate().request(newRequest).build());
        } catch (Exception e) {
            log.warn("用户未登录或Token无效: {}", e.getMessage());
            // 未登录时也放行，由下游服务决定是否需要登录
            // 如需强制登录，可以在这里返回401
            return chain.filter(exchange);
        }
    }

    /**
     * 判断是否是白名单路径
     */
    private boolean isWhitePath(String path) {
        for (String pattern : WHITE_LIST) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}

