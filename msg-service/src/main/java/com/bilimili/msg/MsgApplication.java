package com.bilimili.msg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Description:
 *
 * @author Yury
 * @date 2024/08/23 11:52
 */
@SpringBootApplication
@MapperScan({"com.bilimili.msg.mapper"})
@EnableDiscoveryClient
@EnableFeignClients
public class MsgApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsgApplication.class, args);
    }
}
