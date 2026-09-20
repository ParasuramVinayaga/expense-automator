package com.app.expenseautomator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.expenseautomator.dtos.user.CreateUserRequest;
import com.app.expenseautomator.dtos.user.UpdateUserRequest;
import com.app.expenseautomator.dtos.user.UserResponse;
import com.app.expenseautomator.entity.User;
import com.app.expenseautomator.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }
    
    @PostMapping
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest user) {
        return new UserResponse(service.registerUser(user));
    }

    @GetMapping
    public UserResponse getUser() {
        User user = service.getAuthenticatedUser();
        return new UserResponse(user);
    }

    @PatchMapping
    public UserResponse updateUser(@Valid @RequestBody UpdateUserRequest updateRequest) {
        User user = service.updateUserById(updateRequest);
        return new UserResponse(user);
    }

    @DeleteMapping
    public ResponseEntity<Object> removeUser() {
        service.deleteUserById();
        return ResponseEntity.noContent().build();
    }
}
