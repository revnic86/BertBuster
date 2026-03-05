package com.rob.bertbuster.domain.entity.dto;

import com.rob.bertbuster.domain.entity.MovieGenre;
import com.rob.bertbuster.domain.entity.MovieRating;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

/**
 * DTO for adding a new movie.
 */
public record AddMovieDto(

        @NotBlank(message = "You must enter a title")
        @Size(min = 1, max = 100, message="Title must be between 1 and 100 characters")
        String title,


        @NotNull(message = "You must include a release year")
        @Min(1888)
        @Max(2026)
        Integer releaseYear,

        @NotNull
        MovieGenre movieGenre,

        @NotNull
        MovieRating movieRating,

        @NotNull(message = "You must enter a runtime")
        @Min(1)
        @Max(300)
        Integer runtime

) {
}
