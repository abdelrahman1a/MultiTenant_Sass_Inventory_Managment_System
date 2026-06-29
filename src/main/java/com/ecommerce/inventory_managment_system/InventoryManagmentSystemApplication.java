package com.ecommerce.inventory_managment_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class InventoryManagmentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagmentSystemApplication.class, args);
    }

}
