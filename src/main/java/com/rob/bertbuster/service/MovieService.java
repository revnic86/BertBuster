package com.rob.bertbuster.service;

import com.rob.bertbuster.domain.entity.dto.AddMovieDto;
import com.rob.bertbuster.domain.entity.dto.EditMovieDto;
import com.rob.bertbuster.domain.entity.dto.MovieResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MovieService {

    MovieResponseDto addMovie(AddMovieDto addMovieDto);

    void removeMovieById(UUID uuid);

    MovieResponseDto editMovie(UUID uuid, EditMovieDto editMovieDto);

    Page<MovieResponseDto> getAllMovies(Pageable pageable);


}
