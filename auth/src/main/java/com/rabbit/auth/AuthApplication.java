package com.rabbit.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import com.rabbit.common.feign.UserFeignClient;

/**
 * 认证服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = UserFeignClient.class)
@ComponentScan(basePackages = {
        "com.rabbit.auth",
        "com.rabbit.common.config",
        "com.rabbit.common.exception",
        "com.rabbit.common.feign",
        "com.rabbit.common.utils"
})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}

