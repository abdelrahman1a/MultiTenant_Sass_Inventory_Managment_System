package com.ecommerce.inventory_managment_system.controllers;


import com.ecommerce.inventory_managment_system.common.PageResponse;
import com.ecommerce.inventory_managment_system.responses.TenantResponse;
import com.ecommerce.inventory_managment_system.services.TenantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenant", description = "Tenant API")
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("/approve/{tenant-id}")
    public ResponseEntity<Void> approveTenant(@PathVariable("tenant-id") final String tenantId)
    {
        this.tenantService.approveTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/activate/{tenant-id}")
    public ResponseEntity<Void> activateTenant(@PathVariable("tenant-id") final String tenantId)
    {
        this.tenantService.activateTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate/{tenant-id}")
    public ResponseEntity<Void> deactivateTenant(@PathVariable("tenant-id") final String tenantId)
    {
        this.tenantService.deactivateTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/suspend/{tenant-id}")
    public ResponseEntity<Void> suspendTenant(@PathVariable("tenant-id") final String tenantId)
    {
        this.tenantService.suspendTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<TenantResponse>> finalAllTenants(@RequestParam(name = "page" , defaultValue = "0") final int page ,
                                                                        @RequestParam(name = "size" , defaultValue = "10") final int size)
    {
        return ResponseEntity.ok( this.tenantService.findAll(page , size));
    }

}
