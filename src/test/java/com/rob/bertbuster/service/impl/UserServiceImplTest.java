package com.rob.bertbuster.service.impl;

import com.rob.bertbuster.domain.entity.User;
import com.rob.bertbuster.domain.entity.dto.RegisterUserDto;
import com.rob.bertbuster.domain.entity.dto.UserRegistrationResponseDto;
import com.rob.bertbuster.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUser_newUsername_savesAndReturnsDto() {
        RegisterUserDto dto = new RegisterUserDto("newuser", "pass123", null);

        UserRegistrationResponseDto response = userService.registerUser(dto);

        assertNotNull(response.id());
        assertEquals("newuser", response.username());
        assertEquals("USER", response.role());  // default from mapper

        // Check persisted
        Optional<User> saved = userRepository.findByUsername("newuser");
        assertTrue(saved.isPresent());
        assertEquals("newuser", saved.get().getUsername());
        assertEquals("USER", saved.get().getRole());
        // Password is encoded — we don't assert exact value here
    }

    @Test
    void registerUser_duplicateUsername_throwsException_andDoesNotSave() {
        // Pre-save a user
        User existing = new User();
        existing.setUsername("duplicate");
        existing.setPassword("encoded");
        existing.setRole("USER");
        userRepository.save(existing);

        RegisterUserDto dto = new RegisterUserDto("duplicate", "pass123", null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.registerUser(dto));

        assertEquals("Username already exists", ex.getMessage());

        // No new save happened
        assertEquals(1, userRepository.findAll().size()); // only the pre-existing one
    }


}