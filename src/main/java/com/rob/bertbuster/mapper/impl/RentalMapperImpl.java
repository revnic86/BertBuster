package com.rob.bertbuster.mapper.impl;

import com.rob.bertbuster.constant.AppConstants;
import com.rob.bertbuster.domain.entity.Rental;
import com.rob.bertbuster.domain.entity.dto.RentalResponseDto;
import com.rob.bertbuster.mapper.RentalMapper;
import org.springframework.stereotype.Component;

@Component
public class RentalMapperImpl implements RentalMapper {
    @Override
    public RentalResponseDto rentalToDto(Rental rental) {

        RentalResponseDto rentalResponseDto = new RentalResponseDto(
                rental.getUuid(),
                rental.getDvd().getMovie().getUuid(),
                rental.getDvd().getUuid(),
                rental.getDvd().getBarcode(),
                rental.getBorrowedAt(),
                AppConstants.RENTAL_SUCCESS
        );

        return rentalResponseDto;
    }

}
