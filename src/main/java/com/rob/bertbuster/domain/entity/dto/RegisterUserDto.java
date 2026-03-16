package com.rob.bertbuster.domain.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDto(
        @NotBlank
        String username,

        @NotBlank
        @Size(min=5) //demo only
        String password,
        String role

) {
}
