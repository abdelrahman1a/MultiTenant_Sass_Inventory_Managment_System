package com.ecommerce.inventory_managment_system.auth.service;

import com.ecommerce.inventory_managment_system.auth.requests.LoginRequest;
import com.ecommerce.inventory_managment_system.auth.responses.LoginResponse;
import com.ecommerce.inventory_managment_system.entities.User;
import com.ecommerce.inventory_managment_system.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Override
    public LoginResponse login(LoginRequest request) {
        final Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername() , request.getPassword()));

        final User user = (User) authentication.getPrincipal();

        final String token = this.jwtTokenService.generateAccessToken(user.getTenantId() , user.getId() ,user.getRole().name() );

        final String tokenType = "Bearer";


        return LoginResponse.builder()
                .accessToken(token)
                .tokenType(tokenType)
                .build();
    }
}
