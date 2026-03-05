package com.rob.bertbuster.mapper.impl;

import com.rob.bertbuster.domain.entity.DVD;
import com.rob.bertbuster.domain.entity.DVDStatus;
import com.rob.bertbuster.domain.entity.Movie;
import com.rob.bertbuster.domain.entity.dto.AddMovieDto;
import com.rob.bertbuster.domain.entity.dto.EditMovieDto;
import com.rob.bertbuster.domain.entity.dto.MovieResponseDto;
import com.rob.bertbuster.mapper.MovieMapper;
import org.springframework.stereotype.Component;

@Component
public class MovieMapperImpl implements MovieMapper {


    @Override
    public MovieResponseDto movieToDto(Movie movie) {

        int availableCopies = 0;
        if (movie.getCopies() != null) {
            for (DVD dvd : movie.getCopies()) {
                if (dvd.getDvdStatus() == DVDStatus.AVAILABLE) {
                    availableCopies++;
                }
            }
        }

        MovieResponseDto movieResponseDto = new MovieResponseDto(
                movie.getUuid(),
                movie.getTitle(),
                movie.getReleaseYear(),
                movie.getMovieGenre(),
                movie.getMovieRating(),
                movie.getRuntime(),
                availableCopies
        );

        return movieResponseDto;

    }


    @Override
    public Movie movieFromDto(AddMovieDto addMovieDto) {
        Movie movie = new Movie(
                addMovieDto.title(),
                addMovieDto.movieGenre(),
                addMovieDto.movieRating(),
                addMovieDto.releaseYear(),
                addMovieDto.runtime()

        );

        return movie;

    }


    @Override
    public Movie editMovieFromDto(EditMovieDto editMovieDto) {
        Movie movie = new Movie(
                editMovieDto.title(),
                editMovieDto.movieGenre(),
                editMovieDto.movieRating(),
                editMovieDto.releaseYear(),
                editMovieDto.runtime()

        );

        return movie;
    }

}
