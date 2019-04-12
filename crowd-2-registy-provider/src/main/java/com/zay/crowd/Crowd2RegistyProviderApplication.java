package com.zay.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Crowd2RegistyProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(Crowd2RegistyProviderApplication.class, args);
	}

}
