package com.ecommerce.inventory_managment_system.mapper;

import com.ecommerce.inventory_managment_system.entities.Tenant;
import com.ecommerce.inventory_managment_system.requests.RegisterTenantRequest;
import com.ecommerce.inventory_managment_system.responses.TenantResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TenantMapper {

    public Tenant toEntity(final RegisterTenantRequest request)
    {
        return Tenant.builder()
                .companyName(request.getCompanyName())
                .companyCode(request.getCompanyCode())
                .adminFullName(request.getAdminFullName())
                .adminEmail(request.getAdminEmail())
                .adminUsername(request.getAdminUsername())
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .email(request.getEmail())
                .build();
    }

    public TenantResponse toResponse(final Tenant tenant)
    {
        return TenantResponse.builder()
                .tenantId(tenant.getId())
                .companyName(tenant.getCompanyName())
                .companyCode(tenant.getCompanyCode())
                .adminFullName(tenant.getAdminFullName())
                .adminEmail(tenant.getAdminEmail())
                .adminUsername(tenant.getAdminUsername())
                .email(tenant.getEmail())
                .status(tenant.getTenantStatus())
                .createdAt(tenant.getCreatedAt())
                .build();
    }
}
