package com.open.capacity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 答题中心启动类
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class AnswerCenterApp {
    public static void main(String[] args) {
        SpringApplication.run(AnswerCenterApp.class, args);
    }
}

