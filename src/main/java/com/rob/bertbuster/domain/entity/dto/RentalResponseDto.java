package com.rob.bertbuster.domain.entity.dto;

import java.time.LocalDate;
import java.util.UUID;

public record RentalResponseDto(
        UUID rentalId,
        UUID movieId,
        UUID dvdId,
        String dvdBarcode,
        LocalDate borrowedAt,
        String message   // e.g. "DVD rented successfully"
) { }