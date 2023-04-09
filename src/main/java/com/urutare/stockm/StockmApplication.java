package com.urutare.stockm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class StockmApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockmApplication.class, args);
	}

}
