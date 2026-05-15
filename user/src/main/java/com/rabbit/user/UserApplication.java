package com.rabbit.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import com.rabbit.common.feign.GoodsFeignClient;

/**
 * 用户服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = GoodsFeignClient.class)
@MapperScan("com.rabbit.user.mapper")
@ComponentScan(basePackages = {
        "com.rabbit.user",
        "com.rabbit.common.config",
        "com.rabbit.common.exception",
        "com.rabbit.common.feign",
        "com.rabbit.common.utils"
})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}

