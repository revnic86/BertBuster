package com.rob.bertbuster.service;

import com.rob.bertbuster.domain.entity.dto.RentalResponseDto;

import java.util.UUID;

public interface RentalService {

    RentalResponseDto borrowDVD(UUID movieId);
    RentalResponseDto returnDVD(UUID rentalId);


}
