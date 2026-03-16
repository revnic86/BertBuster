package com.rob.bertbuster.domain.entity.dto;

import com.rob.bertbuster.constant.AppConstants;
import com.rob.bertbuster.domain.entity.MovieGenre;
import com.rob.bertbuster.domain.entity.MovieRating;
import jakarta.validation.constraints.*;

/**
 * DTO for updating an existing movie.
 */
public record EditMovieDto(

        @NotBlank(message = AppConstants.NO_TITLE)
        @Size(min = 1, max = 250, message= AppConstants.INVALID_TITLE_SIZE)
        String title,

        @NotNull(message = AppConstants.NO_RELEASE_YEAR)
        @Min(1888)
        @Max(2026)
        Integer releaseYear,

        @NotNull
        MovieGenre movieGenre,

        @NotNull
        MovieRating movieRating,

        @NotNull(message = AppConstants.NO_RUNTIME)
        @Min(1)
        @Max(300)
        Integer runtime)
{
}
