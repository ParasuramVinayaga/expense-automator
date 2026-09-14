package com.app.expenseautomator.services;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.expenseautomator.dtos.user.CreateUserRequest;
import com.app.expenseautomator.dtos.user.UpdateUserRequest;
import com.app.expenseautomator.entity.User;
import com.app.expenseautomator.exceptions.UserAlreadyExistsException;
import com.app.expenseautomator.exceptions.UserNotFoundException;
import com.app.expenseautomator.repositories.UserRepository;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User registerUser(CreateUserRequest user) {
        String userEmail = user.getEmail();
        if (StringUtils.isNotBlank(userEmail) && repository.existsByEmail(userEmail)) {
            throw new UserAlreadyExistsException(userEmail);
        }

        User newUser = new User();
        newUser.setEmail(userEmail);
        String encodedPassword = (new BCryptPasswordEncoder()).encode(user.getPassword());
        newUser.setPassword(encodedPassword);
        String userName = user.getName();
        newUser.setName(StringUtils.isNotBlank(userName) ? userName : newUser.getNameFromEmail());

        return repository.save(newUser);
    }

    public User getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException());
        return user;
    }

    @Transactional
    public User updateUserById(UpdateUserRequest updateRequest) {
        User user = getAuthenticatedUser();
        String userName = updateRequest.getName();

        if (StringUtils.isNotBlank(userName)) {
            user.setName(userName);
        }

        String password = updateRequest.getPassword();

        if (StringUtils.isNotBlank(password)) {
            user.setPassword(password);
        }

        return repository.save(user);
    }

    @Transactional
    public void deleteUserById() {
        User user = getAuthenticatedUser();
        repository.delete(user);
    }

    public User getAuthenticatedUser() {
        String authEmail = SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getName();

        return repository
        .findByEmail(authEmail)
        .orElseThrow(() -> new UserNotFoundException());
    }
    
}
