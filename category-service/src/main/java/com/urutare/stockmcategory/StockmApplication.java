package com.urutare.stockmcategory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StockmApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockmApplication.class, args);
	}

}
