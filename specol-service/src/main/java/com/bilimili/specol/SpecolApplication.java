package com.bilimili.specol;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Description:
 *
 * @author Yury
 * @date 2024/08/23 09:50
 */
@SpringBootApplication
@MapperScan({"com.bilimili.specol.mapper"})
@EnableDiscoveryClient
@EnableFeignClients
public class SpecolApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpecolApplication.class, args);
    }
}
