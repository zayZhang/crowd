package com.zay.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Crowd4RedisProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(Crowd4RedisProviderApplication.class, args);
	}

}
