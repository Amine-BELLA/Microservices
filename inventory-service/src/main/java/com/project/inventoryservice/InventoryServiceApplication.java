package com.project.inventoryservice;

import com.project.inventoryservice.model.Inventory;
import com.project.inventoryservice.repository.InventoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory1 = new Inventory();
			Inventory inventory2 = new Inventory();

			inventory1.setSkuCode("iphone_15");
			inventory1.setQuantity(100);

			inventory2.setSkuCode("iphone_13");
			inventory2.setQuantity(50);

			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory2);
		};
	}

}
