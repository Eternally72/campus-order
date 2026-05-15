package com.rabbit.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sentinel网关限流配置
 */
@Configuration
public class SentinelGatewayConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler(
            ObjectProvider<List<ViewResolver>> viewResolversProvider,
            ServerCodecConfigurer serverCodecConfigurer) {
        return new SentinelGatewayBlockExceptionHandler(
                viewResolversProvider.getIfAvailable(Collections::emptyList),
                serverCodecConfigurer
        );
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    public void doInit() {
        initCustomizedApis();
        initGatewayRules();
        initBlockHandler();
    }

    /**
     * 初始化API分组
     */
    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();

        // 用户服务API分组
        ApiDefinition userApi = new ApiDefinition("user-api")
                .setPredicateItems(new HashSet<>() {{
                    add(new ApiPathPredicateItem()
                            .setPattern("/api/user/**")
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

        // 商品服务API分组
        ApiDefinition goodsApi = new ApiDefinition("goods-api")
                .setPredicateItems(new HashSet<>() {{
                    add(new ApiPathPredicateItem()
                            .setPattern("/api/goods/**")
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

        // 订单服务API分组
        ApiDefinition orderApi = new ApiDefinition("order-api")
                .setPredicateItems(new HashSet<>() {{
                    add(new ApiPathPredicateItem()
                            .setPattern("/api/order/**")
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

        definitions.add(userApi);
        definitions.add(goodsApi);
        definitions.add(orderApi);

        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    /**
     * 初始化网关限流规则
     */
    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();

        // 用户服务限流：每秒100个请求
        rules.add(new GatewayFlowRule("user-api")
                .setCount(100)
                .setIntervalSec(1));

        // 商品服务限流：每秒200个请求
        rules.add(new GatewayFlowRule("goods-api")
                .setCount(200)
                .setIntervalSec(1));

        // 订单服务限流：每秒50个请求
        rules.add(new GatewayFlowRule("order-api")
                .setCount(50)
                .setIntervalSec(1));

        GatewayRuleManager.loadRules(rules);
    }

    /**
     * 初始化限流异常处理器
     */
    private void initBlockHandler() {
        BlockRequestHandler blockRequestHandler = (exchange, t) ->
                ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("{\"code\":429,\"message\":\"请求过于频繁，请稍后再试\",\"data\":null}");
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }
}

