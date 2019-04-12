package com.zay.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.zay.crowd.mapper")
public class CrowdDatabaseProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdDatabaseProviderApplication.class, args);
    }

}
