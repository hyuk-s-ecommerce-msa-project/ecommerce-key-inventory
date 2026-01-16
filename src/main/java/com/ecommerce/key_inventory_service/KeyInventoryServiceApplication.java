package com.ecommerce.key_inventory_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KeyInventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyInventoryServiceApplication.class, args);
	}

}
