package com.rob.bertbuster.domain.entity.dto;

import com.rob.bertbuster.domain.entity.MovieGenre;
import com.rob.bertbuster.domain.entity.MovieRating;

import java.util.UUID;
/**
 * This DTO will be used as the response to send back to the presentation layer
 */
public record MovieResponseDto(

        UUID uuid,
        String title,
        Integer releaseYear,
        MovieGenre movieGenre,
        MovieRating movieRating,
        Integer runtime,
        Integer availableCopies)

{

}