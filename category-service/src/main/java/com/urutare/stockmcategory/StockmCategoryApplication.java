package com.urutare.stockmcategory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StockmCategoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockmCategoryApplication.class, args);
	}
}
