package com.rob.bertbuster.mapper;

import com.rob.bertbuster.domain.entity.Rental;
import com.rob.bertbuster.domain.entity.dto.RentalResponseDto;

public interface RentalMapper {

    RentalResponseDto rentalToDto(Rental rental);


}
