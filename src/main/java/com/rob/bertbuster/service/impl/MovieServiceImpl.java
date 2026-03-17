package com.rob.bertbuster.service.impl;

import com.rob.bertbuster.domain.entity.DVD;
import com.rob.bertbuster.domain.entity.Movie;
import com.rob.bertbuster.domain.entity.Rental;
import com.rob.bertbuster.domain.entity.dto.AddMovieDto;
import com.rob.bertbuster.domain.entity.dto.EditMovieDto;
import com.rob.bertbuster.domain.entity.dto.MovieResponseDto;
import com.rob.bertbuster.exception.MovieNotFoundException;
import com.rob.bertbuster.mapper.MovieMapper;
import com.rob.bertbuster.repository.MovieRepository;
import com.rob.bertbuster.service.MovieService;
import jakarta.transaction.Transactional;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieServiceImpl implements MovieService {


    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }


    @Transactional
    @Override
    public MovieResponseDto addMovie(AddMovieDto addMovieDto) {

        Movie movie = movieMapper.movieFromDto(addMovieDto);

        for (int x = 0; x < 3; x++){
            DVD dvd = new DVD(addMovieDto.title() + " - " + addMovieDto.releaseYear() + " COPY " + "- " + (x+1));   //creates 3 DVDs for every movie title added
            movie.addDvd(dvd);    //this is needed otherwise the foreign key on DVDs table will be null - syncs both tables
        }

        movieRepository.save(movie);

        MovieResponseDto movieResponseDto = movieMapper.movieToDto(movie);

        return movieResponseDto;

    }

    @Transactional
    @Override
    public void removeMovieById(UUID uuid) {

        /*
        The following block fixes the bug that occurs when a movie can no longer be deleted after it has been rented.

         */
        Movie movie = movieRepository.findById(uuid)
                .orElseThrow(() -> new MovieNotFoundException(uuid));

        for (DVD dvd : movie.getCopies()) {  //ehnanced forloop that goes through each dvd copy

            List<Rental> rentals = new ArrayList<>(dvd.getRentals());

             for (Rental rental : rentals) { //enhanced forloop that goes through each rental record, sets the dvd to null fix foreign key issue
                     rental.setDvd(null);
              }


        }

        movieRepository.deleteById(uuid);
    }

    @Transactional
    @Override
    public MovieResponseDto editMovie(UUID uuid, EditMovieDto editMovieDto) {

        Optional<Movie> byId = movieRepository.findById(uuid);

        if (byId.isPresent()){
            Movie movie = byId.get();

            // Capture old values BEFORE changing them for DVD table updates
            String oldTitle = movie.getTitle();
            Integer oldYear = movie.getReleaseYear();

            movie.setTitle(editMovieDto.title());
            movie.setMovieGenre(editMovieDto.movieGenre());
            movie.setMovieRating(editMovieDto.movieRating());
            movie.setReleaseYear(editMovieDto.releaseYear());
            movie.setRuntime(editMovieDto.runtime());

            // If title or year actually changed then regenerate barcodes for all DVDs
            if (!oldTitle.equals(editMovieDto.title()) || !oldYear.equals(editMovieDto.releaseYear())) {
                int copyNumber = 1;
                for (DVD dvd : movie.getCopies()) {
                    dvd.setBarcode(editMovieDto.title() + " - " + editMovieDto.releaseYear() + " COPY - " + copyNumber);
                    copyNumber++;
                }
            }


            Movie save = movieRepository.save(movie);

            return movieMapper.movieToDto(movie);


        }
        else {
            throw new MovieNotFoundException(uuid);
        }
    }

    @Override
    public Page<MovieResponseDto> getAllMovies(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);

        return moviePage.map(movieMapper::movieToDto);
    }


    //pageable not working with AI
    @McpTool(description = "Retrieve all movies in the database")
    public List<MovieResponseDto> getAllMoviesForAi() {
               return movieRepository.findAll().stream()
                .map(movieMapper::movieToDto)
                .toList();
    }

}
