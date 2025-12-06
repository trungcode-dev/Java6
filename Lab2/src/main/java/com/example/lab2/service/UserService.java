package com.example.lab2.service;

import com.example.lab2.entity.User;
import com.example.lab2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        String password = user.getPassword();
        String[] roles = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getId().substring(5))
                .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(password)
                .roles(roles)
                .build();
    }
}
