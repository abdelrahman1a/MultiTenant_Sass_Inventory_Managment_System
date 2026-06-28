package com.ecommerce.inventory_managment_system.repositories;

import com.ecommerce.inventory_managment_system.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category , String> {

    Optional<Category> findByNameIgnoreCase(String name);
}
