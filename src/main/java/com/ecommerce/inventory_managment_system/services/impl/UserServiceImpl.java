package com.ecommerce.inventory_managment_system.services.impl;

import com.ecommerce.inventory_managment_system.common.PageResponse;
import com.ecommerce.inventory_managment_system.config.TenantContext;
import com.ecommerce.inventory_managment_system.entities.User;
import com.ecommerce.inventory_managment_system.entities.UserRole;
import com.ecommerce.inventory_managment_system.exceptions.DuplicateResourceException;
import com.ecommerce.inventory_managment_system.exceptions.InvalidRequestException;
import com.ecommerce.inventory_managment_system.mapper.UserMapper;
import com.ecommerce.inventory_managment_system.repositories.TenantRepository;
import com.ecommerce.inventory_managment_system.repositories.UserRepository;
import com.ecommerce.inventory_managment_system.requests.UserRequest;
import com.ecommerce.inventory_managment_system.responses.UserResponse;
import com.ecommerce.inventory_managment_system.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TenantRepository tenantRepository;
    private final UserMapper userMapper;



    @Override
    public void createUser(UserRequest request) {
        final String tenantId = TenantContext.getCurrentTenant();
        log.info("Creating user for tenant: {}", tenantId);


        // validate if username exists
        if(this.repository.existsByUsername(request.getUsername()))
        {
            throw new DuplicateResourceException("Username already exists");
        }

        if(this.repository.existsByEmail(request.getEmail()))
        {
            throw new DuplicateResourceException("Email already exists");
        }

        if(request.getRole() == UserRole.ROLE_PLATFORM_ADMIN)
        {
            throw new InvalidRequestException("Role is required");
        }

        final User user = this.userMapper.toEntity(request);
    }

    @Override
    public void updateUser(String userId, UserRequest request) {
        final String tenantId = TenantContext.getCurrentTenant();
        log.info("Updating user for tenant: {}", tenantId);

        User user = this.repository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("user doesn't exist"));

        if (!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        // check if username is being changed and if it is already taken
        if(!user.getTenant().getId().equals(tenantId) && this.repository.existsByUsername(request.getUsername()))
        {
            throw new DuplicateResourceException("Username already exists");

        }


        // check if email is being changed and if it is already taken
        if (!user.getEmail().equals(request.getEmail()) && this.repository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        // validate role (cannot be PLATFORM_ADMIN)
        if (request.getRole() == UserRole.ROLE_PLATFORM_ADMIN) {
            throw new InvalidRequestException("Role is required");
        }

        // update user details
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        this.repository.save(user);
        log.info("User updated successfully");

    }

    @Override
    public void deleteUser(String userId) {
        final String tenantId = TenantContext.getCurrentTenant();
        log.info("Deleting user for tenant: {}", tenantId);

        User user = this.repository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("user doesn't exist"));

        // check if user belongs to the tenant
        if (!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        // soft delete
        user.setDeleted(true);
        this.repository.save(user);
        log.info("User deleted successfully");

    }

    @Override
    public UserResponse getUserById(String userId) {
        final String tenantId = TenantContext.getCurrentTenant();

        User user = this.repository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("user doesn't exist"));

        // check if user belong to tenant
        if(!user.getTenant().getId().equals(tenantId))
        {
            throw new InvalidRequestException("User does not belong to the tenant");
        }
        return this.userMapper.toResponse(user);


    }

    @Override
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        final String tenantId = TenantContext.getCurrentTenant();
        final PageRequest pageRequest = PageRequest.of(page , size);
        final Page<User> userPage = this.repository.findAllByTenantId(tenantId , pageRequest);
        Page<UserResponse> userResponses = userPage.map(this.userMapper::toResponse);

        return PageResponse.of(userResponses);
    }

    @Override
    public void enableUser(String userId) {
        final String tenantId = TenantContext.getCurrentTenant();

        User user = this.repository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("user doesn't exist"));

        // check if user belongs to the tenant
        if (!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        user.setEnabled(true);
        this.repository.save(user);
        log.info("User enabled successfully");
    }

    @Override
    public void disableUser(String userId) {
        final String tenantId = TenantContext.getCurrentTenant();

        User user = this.repository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("user doesn't exist"));

        // check if user belongs to the tenant
        if (!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        user.setEnabled(false);
        this.repository.save(user);
        log.info("User disabled successfully");

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("No user was found with: " + username));
    }
}
