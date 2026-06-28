package com.ecommerce.inventory_managment_system.controllers;

import com.ecommerce.inventory_managment_system.common.PageResponse;
import com.ecommerce.inventory_managment_system.requests.UserRequest;
import com.ecommerce.inventory_managment_system.responses.UserResponse;
import com.ecommerce.inventory_managment_system.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name ="User"  , description = "User API")
public class UserController {

    private final UserService userService;


    @PostMapping
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> createUser(@Valid @RequestBody final UserRequest request)
    {
        this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(name = "page" , defaultValue = "0") final int page ,
            @RequestParam(name = "size", defaultValue = "10")
            final int size)
    {
        final PageResponse<UserResponse> response = this.userService.getAllUsers(page , size);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{user-id}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("user-id") final String userId)
    {
        final UserResponse response = this.userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{user-id}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("user-id") final String userId ,
                                                   @Valid @RequestBody UserRequest request)
    {
         this.userService.updateUser(userId , request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/{user-id}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") final String userId )
    {
        this.userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{user-id}/enable")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> enableUser(
            @PathVariable("user-id")
            final String userId) {
        this.userService.enableUser(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @PutMapping("/{user-id}/disable")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> disableUser(
            @PathVariable("user-id")
            final String userId) {
        this.userService.disableUser(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }





}
