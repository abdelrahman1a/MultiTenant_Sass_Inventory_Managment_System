package com.ecommerce.inventory_managment_system.services.impl;

import com.ecommerce.inventory_managment_system.common.PageResponse;
import com.ecommerce.inventory_managment_system.entities.Tenant;
import com.ecommerce.inventory_managment_system.entities.TenantStatus;
import com.ecommerce.inventory_managment_system.entities.User;
import com.ecommerce.inventory_managment_system.entities.UserRole;
import com.ecommerce.inventory_managment_system.exceptions.DuplicateResourceException;
import com.ecommerce.inventory_managment_system.exceptions.InvalidRequestException;
import com.ecommerce.inventory_managment_system.mapper.TenantMapper;
import com.ecommerce.inventory_managment_system.repositories.TenantRepository;
import com.ecommerce.inventory_managment_system.repositories.UserRepository;
import com.ecommerce.inventory_managment_system.requests.RegisterTenantRequest;
import com.ecommerce.inventory_managment_system.responses.TenantResponse;
import com.ecommerce.inventory_managment_system.services.ProvisionService;
import com.ecommerce.inventory_managment_system.services.TenantService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantServiceImpl implements TenantService {

   private final TenantRepository tenantRepository;

    private final TenantMapper tenantMapper;

    private final PasswordEncoder passwordEncoder;

    private final ProvisionService provisionService;

    private final UserRepository userRepository;


    @Override
    @Transactional
    public void registerTenant(RegisterTenantRequest request) {
        // check if tenant exists by company code
        if(tenantRepository.existsByCompanyCode(request.getCompanyCode()))
        {
            throw new DuplicateResourceException("Tenant already exists");
        }

        // check if tenant exists by email
        if(tenantRepository.existsByEmail(request.getEmail()))
        {
            throw new DuplicateResourceException("Tenant Email already exists");
        }

        // create tenant
        final Tenant tenant = this.tenantMapper.toEntity(request);
        tenant.setAdminPassword(passwordEncoder.encode(request.getAdminPassword()));
        tenant.setTenantStatus(TenantStatus.PENDING);
        this.tenantRepository.save(tenant);

    }

    @Override
    public void approveTenant(final String tenantId) {

        // check if tenant Exists or not
       final Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Tenant does not exist"));

        tenant.setTenantStatus(TenantStatus.ACTIVE);
        this.tenantRepository.save(tenant);

        try{
            // provision schema for tenant
            provisionService.provisionTenant(tenant);
            // create initial adminUser
            createInitialAdminUser(tenant);
        } catch (Exception e) {
            rollbackTenantStatus(tenant);
        }
    }

    private void createInitialAdminUser(final Tenant tenant)
    {
        // check if the user already exists
        if(this.userRepository.existsByUsername(tenant.getAdminUsername()))
        {
            throw new DuplicateResourceException("User already exists");
        }

        final User adminUser = User.builder()
                .username(tenant.getAdminUsername())
                .email(tenant.getAdminEmail())
                .firstName(extractFirstName(tenant.getAdminFullName()))
                .lastName(extractLastName(tenant.getAdminFullName()))
                .password(tenant.getAdminPassword())
                .role(UserRole.ROLE_COMPANY_ADMIN)
                .tenant(tenant)
                .enabled(true)
                .build();
        this.userRepository.save(adminUser);
        log.info("Created initial admin user for tenant {}", tenant.getId());


    }


    private String extractFirstName(final String fullName) {
        return fullName.split(" ")[0];
    }

    private String extractLastName(final String fullName) {
        return fullName.split(" ").length > 1 ? fullName.split(" ")[1] : fullName;
    }

    private void rollbackTenantStatus(final Tenant tenant)
    {
        tenant.setTenantStatus(TenantStatus.PENDING);
        this.tenantRepository.save(tenant);
    }

    @Override
    public void activateTenant(final String tenantId) {
        final Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Tenant does not exist"));

        if (tenant.getTenantStatus() != TenantStatus.PENDING) {
            throw new InvalidRequestException("Tenant is not pending");
        }


        tenant.setTenantStatus(TenantStatus.ACTIVE);
        this.tenantRepository.save(tenant);
    }

    @Override
    public void deactivateTenant(String tenantId) {

       final Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Tenant does not exist"));

        if (tenant.getTenantStatus() != TenantStatus.ACTIVE) {
            throw new InvalidRequestException("Tenant is not pending");
        }

        tenant.setTenantStatus(TenantStatus.INACTIVE);
        this.tenantRepository.save(tenant);

    }

    @Override
    public void suspendTenant(String tenantId) {
        final Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Tenant does not exist"));

        if (tenant.getTenantStatus() != TenantStatus.ACTIVE) {
            throw new InvalidRequestException("Tenant is not pending");
        }

        tenant.setTenantStatus(TenantStatus.SUSPENDED);
        this.tenantRepository.save(tenant);

    }

    @Override
    public PageResponse<TenantResponse> findAll(int page, int size) {
        final PageRequest pageRequest = PageRequest.of(page , size);
        final Page<Tenant> tenants = this.tenantRepository.findAll(pageRequest);
        final Page<TenantResponse> tenantResponses = tenants.map(this.tenantMapper::toResponse);
        return PageResponse.of(tenantResponses) ;
    }
}
