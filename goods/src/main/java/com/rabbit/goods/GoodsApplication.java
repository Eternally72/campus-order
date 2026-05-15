package com.rabbit.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 商品服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.rabbit.goods.mapper")
@ComponentScan(basePackages = {
        "com.rabbit.goods",
        "com.rabbit.common.config",
        "com.rabbit.common.exception",
        "com.rabbit.common.utils"
})
public class GoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }
}

