package com.rob.bertbuster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rob.bertbuster.domain.entity.dto.RentalResponseDto;
import com.rob.bertbuster.mapper.RentalMapper;
import com.rob.bertbuster.repository.RentalRepository;
import com.rob.bertbuster.repository.UserRepository;
import com.rob.bertbuster.service.RentalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;


import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RentalController.class)
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RentalService rentalService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RentalRepository rentalRepository;

    @MockitoBean
    private RentalMapper rentalMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();


    //simulates a logged in user
    private void mockAuthenticatedUser(String username) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);
    }


    @Test
    void borrowDVD_validRequest_returns200AndDto() throws Exception {
        UUID movieId = UUID.randomUUID();
        UUID rentalId = UUID.randomUUID();
        UUID dvdId = UUID.randomUUID();

        RentalResponseDto response = new RentalResponseDto(
                rentalId,
                movieId,
                dvdId,
                "Inception - 2010 COPY - 2",   // dvdBarcode
                LocalDate.now(),
                "Successfully rented"
        );

        when(rentalService.borrowDVD(movieId)).thenReturn(response);

        mockAuthenticatedUser("testuser");

        mockMvc.perform(post("/api/rent/{movieId}", movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rentalId").value(rentalId.toString()))
                .andExpect(jsonPath("$.movieId").value(movieId.toString()))
                .andExpect(jsonPath("$.dvdId").value(dvdId.toString()))
                .andExpect(jsonPath("$.dvdBarcode").value("Inception - 2010 COPY - 2"))
                .andExpect(jsonPath("$.message").value("Successfully rented"))
                .andExpect(jsonPath("$.borrowedAt").exists());  // just check it's there

        verify(rentalService).borrowDVD(movieId);
    }

    @Test
    void returnDVD_validRequest_returns200AndDto() throws Exception {
        UUID rentalId = UUID.randomUUID();

        RentalResponseDto response = new RentalResponseDto(
                rentalId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Barcode",
                LocalDate.now(),                // ← return time
                "DVD returned successfully"
        );
        when(rentalService.returnDVD(rentalId)).thenReturn(response);

        mockAuthenticatedUser("testuser");

        mockMvc.perform(post("/api/return/{rentalId}", rentalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.borrowedAt").isNotEmpty())
                .andExpect(jsonPath("$.message").value("DVD returned successfully"));

        verify(rentalService).returnDVD(rentalId);
    }
}