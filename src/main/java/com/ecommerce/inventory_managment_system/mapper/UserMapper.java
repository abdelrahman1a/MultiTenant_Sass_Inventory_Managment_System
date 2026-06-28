package com.ecommerce.inventory_managment_system.mapper;

import com.ecommerce.inventory_managment_system.entities.User;
import com.ecommerce.inventory_managment_system.requests.UserRequest;
import com.ecommerce.inventory_managment_system.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toEntity(final UserRequest request)
    {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .build();
    }

    public UserResponse toResponse(final User user)
    {
        return  UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .password(user.getPassword())
                .build();
    }
}
