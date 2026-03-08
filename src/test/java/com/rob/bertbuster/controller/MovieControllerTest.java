package com.rob.bertbuster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rob.bertbuster.domain.entity.MovieGenre;
import com.rob.bertbuster.domain.entity.MovieRating;
import com.rob.bertbuster.domain.entity.dto.AddMovieDto;
import com.rob.bertbuster.domain.entity.dto.EditMovieDto;
import com.rob.bertbuster.domain.entity.dto.MovieResponseDto;
import com.rob.bertbuster.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createMovie_validInput_returns201AndResponse() throws Exception {

        //create data
        AddMovieDto addMovieDto = new AddMovieDto("Inception", 2010, MovieGenre.SCIFI, MovieRating.PG13, 148);

        MovieResponseDto response = new MovieResponseDto(
                UUID.randomUUID(), "Inception", 2010, MovieGenre.SCIFI, MovieRating.PG13, 148, 3);

        when(movieService.addMovie(any(AddMovieDto.class))).thenReturn(response);

        String json = objectMapper.writeValueAsString(addMovieDto);

        //post and check
        mockMvc.perform(post("/api/addmovie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.availableCopies").value(3));
    }

    @Test
    void updateMovie_validInput_returnsOkWithUpdatedData() throws Exception {
        UUID movieId = UUID.randomUUID();

        EditMovieDto editMovieDto = new EditMovieDto(
                "Inception (Updated)",
                2010,
                MovieGenre.SCIFI,
                MovieRating.PG13,
                160
        );

        MovieResponseDto updatedResponse = new MovieResponseDto(
                movieId,
                "Inception (Updated)",
                2010,
                MovieGenre.SCIFI,
                MovieRating.PG13,
                160,
                3  // available copies unchanged or whatever your logic does
        );

        when(movieService.editMovie(eq(movieId), any(EditMovieDto.class)))
                .thenReturn(updatedResponse);

        String json = objectMapper.writeValueAsString(editMovieDto);

        mockMvc.perform(put("/api/movie/{uuid}", movieId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(movieId.toString()))
                .andExpect(jsonPath("$.title").value("Inception (Updated)"))
                .andExpect(jsonPath("$.runtime").value(160))
                .andExpect(jsonPath("$.availableCopies").value(3));

        verify(movieService).editMovie(eq(movieId), any(EditMovieDto.class));
    }


    @Test
    void deleteMovieById_returnsNoContent() throws Exception {

        //random uuid
        UUID movieId = UUID.randomUUID();

        doNothing().when(movieService).removeMovieById(movieId); //used "doNothing" for void method

        mockMvc.perform(delete("/api/movie/{uuid}", movieId)) //deletes
                .andExpect(status().isNoContent()); //check 204 is returned

        verify(movieService).removeMovieById(movieId); //checks service method
    }

}