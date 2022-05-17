package com.xiamu.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.xiamu.user.mapper")
public class XiamuUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiamuUserApplication.class);
    }
}
