package com.project.flightbooking.service;

import com.project.flightbooking.entity.User;
import com.project.flightbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        
        // Check if user is inactive
        if ("INACTIVE".equals(user.getStatus())) {
            throw new UsernameNotFoundException("User account is inactive: " + username);
        }
        
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase());
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), 
            user.getPasswordHash(),
            Collections.singleton(authority)
        );
    }
} 