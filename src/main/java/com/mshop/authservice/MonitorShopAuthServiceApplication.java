package com.mshop.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MonitorShopAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorShopAuthServiceApplication.class, args);
	}

}
