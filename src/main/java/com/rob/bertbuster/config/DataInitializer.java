package com.rob.bertbuster.config;

import com.rob.bertbuster.constant.AppConstants;
import com.rob.bertbuster.domain.entity.User;
import com.rob.bertbuster.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername(AppConstants.ADMIN_USER).isEmpty()) {
            User admin = new User(
                    AppConstants.ADMIN_USER,
                    passwordEncoder.encode(AppConstants.ADMIN_PASS),
                    AppConstants.ADMIN_ROLE
            );
            userRepository.save(admin);
            System.out.println(AppConstants.ADMIN_SUCCESS);
        }
    }
}