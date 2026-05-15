package com.rabbit.notify;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 通知服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.rabbit.notify.mapper")
@ComponentScan(basePackages = {
        "com.rabbit.notify",
        "com.rabbit.common.config",
        "com.rabbit.common.exception",
        "com.rabbit.common.utils"
})
public class NotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifyApplication.class, args);
    }
}

