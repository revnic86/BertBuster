package com.rob.bertbuster.service;

import com.rob.bertbuster.domain.entity.User;
import com.rob.bertbuster.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    //builds userDetail objects from the DB to use for a login attempt
    //without this DB wouldnt be checked
    //all OOTB code except for line 23 and 24.
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().split(","))
                .build();
    }
}
