package com.ecommerce.inventory_managment_system.auth.service;

import com.ecommerce.inventory_managment_system.auth.requests.LoginRequest;
import com.ecommerce.inventory_managment_system.auth.responses.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(final LoginRequest request);
}
