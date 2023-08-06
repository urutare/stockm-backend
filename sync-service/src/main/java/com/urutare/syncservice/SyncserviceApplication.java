package com.urutare.syncservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SyncserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SyncserviceApplication.class, args);
	}

}
