package com.ecommerce.inventory_managment_system.repositories;

import com.ecommerce.inventory_managment_system.entities.StockMvt;
import com.ecommerce.inventory_managment_system.entities.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TenantRepository extends JpaRepository<Tenant, String> {
    boolean existsByCompanyCode(String companyCode);

    boolean existsByEmail(String email);
}
