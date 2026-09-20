package com.app.expenseautomator.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.expenseautomator.repositories.UserRepository;


@Service
public class UserAuthDetailService implements UserDetailsService {
    
    private final UserRepository userRepo;

    public UserAuthDetailService (UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.app.expenseautomator.entity.User user = userRepo
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(
            String.format("User %s not found", email)
        ));

        return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .build();
    }
}
