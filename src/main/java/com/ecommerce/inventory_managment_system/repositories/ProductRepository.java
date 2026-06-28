package com.ecommerce.inventory_managment_system.repositories;

import com.ecommerce.inventory_managment_system.entities.Category;
import com.ecommerce.inventory_managment_system.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product , String> {

    Optional<Product> findByReferenceIgnoreCase(String reference);
}
