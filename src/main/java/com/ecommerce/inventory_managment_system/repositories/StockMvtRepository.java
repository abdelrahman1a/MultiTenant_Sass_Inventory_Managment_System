package com.ecommerce.inventory_managment_system.repositories;

import com.ecommerce.inventory_managment_system.entities.StockMvt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StockMvtRepository extends JpaRepository<StockMvt , String> {
    Page<StockMvt> findAllByProductId(String productId , Pageable pageable);
}
