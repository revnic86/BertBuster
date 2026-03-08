package com.rob.bertbuster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rob.bertbuster.domain.entity.dto.RegisterUserDto;
import com.rob.bertbuster.domain.entity.dto.UserRegistrationResponseDto;
import com.rob.bertbuster.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register_validInput_returns201AndResponse() throws Exception {
        // Input from frontend (role currently not being used in front end)
        RegisterUserDto input = new RegisterUserDto(
                "testuser",
                "secret123",
                null
        );

        // Mocked response – backend assigns "USER" role
        UserRegistrationResponseDto response = new UserRegistrationResponseDto(
                UUID.randomUUID(),
                "testuser",
                "USER"   // ← realistic role value
        );

        when(userService.registerUser(any(RegisterUserDto.class))).thenReturn(response);

        String json = objectMapper.writeValueAsString(input);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void register_passwordTooShort_returns400() throws Exception {
        String invalidJson = """
                {
                    "username": "testuser",
                    "password": "123"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    void register_missingUsername_returns400() throws Exception {
        String invalidJson = """
                {
                    "password": "secret123"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}