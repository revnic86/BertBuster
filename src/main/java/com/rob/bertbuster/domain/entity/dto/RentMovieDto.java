package com.rob.bertbuster.domain.entity.dto;

import com.rob.bertbuster.domain.entity.DVD;
import com.rob.bertbuster.domain.entity.User;

import java.time.LocalDate;

public record RentMovieDto(
        User user,
        DVD dvd,
        LocalDate borrowedAt
) {
}
