package com.bilimili.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Description:
 *
 * @author Yury
 * @date 2024/08/23 15:42
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
//@MapperScan({"com.bilimili.demo.mapper"})
@EnableFeignClients
@CrossOrigin(origins = "http://10.192.95.26:31001/", allowCredentials = "true")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
