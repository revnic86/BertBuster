package com.rob.bertbuster.service.impl;

import com.rob.bertbuster.domain.entity.*;
import com.rob.bertbuster.domain.entity.dto.AddMovieDto;
import com.rob.bertbuster.domain.entity.dto.EditMovieDto;
import com.rob.bertbuster.domain.entity.dto.MovieResponseDto;
import com.rob.bertbuster.exception.MovieNotFoundException;
import com.rob.bertbuster.repository.DVDRepository;
import com.rob.bertbuster.repository.MovieRepository;
import com.rob.bertbuster.service.MovieService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MovieServiceImplTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private DVDRepository dvdRepository;

    @Test
    void addMovie_createsMovieWithThreeDvds_allAvailable() {
        AddMovieDto dto = new AddMovieDto(
                "Inception",
                2010,
                MovieGenre.SCIFI,
                MovieRating.PG13,
                148
        );

        MovieResponseDto response = movieService.addMovie(dto);

        assertNotNull(response.uuid());     //check response for uuid, title, and copies
        assertEquals("Inception", response.title());
        assertEquals(3, response.availableCopies());
    }



    @Test
    void addMovie_persistsMovie() {
        AddMovieDto dto = new AddMovieDto(
                "The Matrix",
                1999,
                MovieGenre.ACTION,
                MovieRating.R,
                136
        );



        MovieResponseDto movieResponseDto = movieService.addMovie(dto); //create a new movie


        Movie movie = movieRepository.findById(movieResponseDto.uuid()).orElseThrow(); //retrieve from DB

        assertEquals("The Matrix", movie.getTitle()); //check object created correctly
        assertEquals(1999, movie.getReleaseYear());
        assertEquals(3, movie.getCopies().size());

    }

    @Test
    void removeMovieById_existingId_deletesMovie() {

        // create a movie first
        AddMovieDto dto = new AddMovieDto("Airplane", 1989, MovieGenre.COMEDY, MovieRating.PG, 90);
        MovieResponseDto created = movieService.addMovie(dto);
        UUID id = created.uuid();

        movieService.removeMovieById(id); //remove movie from DB

        assertFalse(movieRepository.existsById(id)); //check the movie does not exist
    }

    @Test
    void removeMovieById_nonExisting_throwsNotFound() {
        UUID random = UUID.randomUUID();
        assertThrows(MovieNotFoundException.class, () -> movieService.removeMovieById(random)); //make sure this exception is thrown if the movie doesn't exist
    }

    @Test
    void removeMovieById_existingMovie_deletesMovieAndItsDvds() {

        //create mock data

        AddMovieDto dto = new AddMovieDto(
                "Top Gun",
                1986,
                MovieGenre.DRAMA,
                MovieRating.PG,
                120
        );

        MovieResponseDto movieResponseDto = movieService.addMovie(dto); // add movie to db
        UUID movieId = movieResponseDto.uuid();

        // Verify it was saved + has 3 DVDs
        assertTrue(movieRepository.existsById(movieId));
        assertEquals(3, movieRepository.findById(movieId).orElseThrow().getCopies().size());


        movieService.removeMovieById(movieId); //delete the movie


        assertFalse(movieRepository.existsById(movieId)); //check movie is gone



        //check dvd is gone from DVD repo

        long remainingDvds = dvdRepository.findAll().stream()
                .filter(dvd -> dvd.getMovie().getUuid().equals(movieId)) //for each dvd check if the UUID equals the deleted movie.
                .count();
        assertEquals(0, remainingDvds, "All DVDs should be deleted when movie is removed");
    }

    @Test
    void editMovie_existingMovie_updatesFieldsAndReturnsCorrectDto() {

        // create mock data
        AddMovieDto createDto = new AddMovieDto(
                "Titanic",
                1997,
                MovieGenre.DRAMA,
                MovieRating.PG13,
                180
        );


        MovieResponseDto created = movieService.addMovie(createDto);
        UUID movieId = created.uuid();


        EditMovieDto editDto = new EditMovieDto(
                "The Departed",
                2005,
                MovieGenre.DRAMA,
                MovieRating.R,
                150
        );

        // call editMovie
        MovieResponseDto updated = movieService.editMovie(movieId, editDto);

        // assert
        assertEquals("The Departed", updated.title());
        assertEquals(2005, updated.releaseYear());
        assertEquals(MovieGenre.DRAMA, updated.movieGenre());
        assertEquals(MovieRating.R, updated.movieRating());
        assertEquals(150, updated.runtime());



        // check db persistence
        Movie savedMovie = movieRepository.findById(movieId).orElseThrow();

        assertEquals("The Departed", savedMovie.getTitle());
        assertEquals(2005, savedMovie.getReleaseYear());
        assertEquals(MovieGenre.DRAMA, savedMovie.getMovieGenre());
        assertEquals(MovieRating.R, savedMovie.getMovieRating());
        assertEquals(150, savedMovie.getRuntime());

        // dvd count remains the same
        assertEquals(3, savedMovie.getCopies().size());
    }

}

