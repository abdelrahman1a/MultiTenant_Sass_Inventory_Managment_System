package com.ecommerce.inventory_managment_system.auth;


import com.ecommerce.inventory_managment_system.auth.requests.LoginRequest;
import com.ecommerce.inventory_managment_system.auth.responses.LoginResponse;
import com.ecommerce.inventory_managment_system.auth.service.AuthenticationService;
import com.ecommerce.inventory_managment_system.requests.RegisterTenantRequest;
import com.ecommerce.inventory_managment_system.services.TenantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication" , description = "Authentication API")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TenantService tenantService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request)
    {
        final LoginResponse response = this.authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid
            @RequestBody
            final RegisterTenantRequest request
    ) {
        this.tenantService.registerTenant(request);
        return ResponseEntity.ok().build();
    }
}
