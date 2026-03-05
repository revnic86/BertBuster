package com.rob.bertbuster.mapper;

import com.rob.bertbuster.domain.entity.Movie;
import com.rob.bertbuster.domain.entity.dto.AddMovieDto;
import com.rob.bertbuster.domain.entity.dto.EditMovieDto;
import com.rob.bertbuster.domain.entity.dto.MovieResponseDto;

public interface MovieMapper {
    MovieResponseDto movieToDto(Movie movie);

    Movie movieFromDto(AddMovieDto addMovieDto);

    Movie editMovieFromDto(EditMovieDto editMovieDto);

}
