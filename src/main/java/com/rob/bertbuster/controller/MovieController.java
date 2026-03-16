package com.rob.bertbuster.controller;

import com.rob.bertbuster.domain.entity.dto.AddMovieDto;
import com.rob.bertbuster.domain.entity.dto.EditMovieDto;
import com.rob.bertbuster.domain.entity.dto.MovieResponseDto;
import com.rob.bertbuster.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }


    @GetMapping("/movie")
    public ResponseEntity<Page<MovieResponseDto>> getAllMovies(Pageable pageable){
        return ResponseEntity.ok(movieService.getAllMovies(pageable));

    }

    @PostMapping("/movie")
    public ResponseEntity<MovieResponseDto> addNewMovie(@Valid @RequestBody AddMovieDto addMovieDto){
        MovieResponseDto movieResponseDto = movieService.addMovie(addMovieDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(movieResponseDto);
    }

    @PutMapping("/movie/{uuid}")
    public ResponseEntity<MovieResponseDto> updateMovie(@Valid @PathVariable UUID uuid, @Valid @RequestBody EditMovieDto editMovieDto){
        MovieResponseDto movieResponseDto = movieService.editMovie(uuid, editMovieDto);

        return ResponseEntity.ok(movieResponseDto);

    }

    @DeleteMapping("/movie/{uuid}")
    public ResponseEntity<Void> deleteMovieById(@Valid @PathVariable UUID uuid){
        movieService.removeMovieById(uuid);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
