package com.bilimili.post;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Description:
 *
 * @author Yury
 * @date 2024/08/21 21:09
 */

@SpringBootApplication
@MapperScan({"com.bilimili.post.mapper"})
@EnableDiscoveryClient
@EnableFeignClients
public class PostApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostApplication.class, args);
    }
}