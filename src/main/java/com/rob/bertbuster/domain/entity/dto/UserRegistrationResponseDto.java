package com.rob.bertbuster.domain.entity.dto;

import java.util.UUID;

public record UserRegistrationResponseDto(
        UUID id,
        String username,
        String role
) {}