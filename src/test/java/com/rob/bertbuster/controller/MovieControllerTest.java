package com.rob.bertbuster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rob.bertbuster.domain.entity.MovieGenre;
import com.rob.bertbuster.domain.entity.MovieRating;
import com.rob.bertbuster.domain.entity.dto.AddMovieDto;
import com.rob.bertbuster.domain.entity.dto.MovieResponseDto;
import com.rob.bertbuster.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class MovieControllerTest {

    //integration test with mockMvc

    private MockMvc mockMvc; //fake http client

    @Autowired
    private WebApplicationContext context;  //inject real web context of app

    @MockitoBean
    private MovieService movieService;

    private final ObjectMapper objectMapper = new ObjectMapper();  //converts dto from object to json later on

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();  // initializes mockMvc
    }

    @Test
    void createMovie_validInput_returns201AndResponse() throws Exception {
        //endpoint unit test

        AddMovieDto dto = new AddMovieDto(              //creating object
                "Inception",
                2010,
                MovieGenre.SCIFI,
                MovieRating.PG13,
                148
        );

        String json = objectMapper.writeValueAsString(dto); //maps to json

        mockMvc.perform(post("/api/addmovie") //runs HTTP post request
                        .contentType(MediaType.APPLICATION_JSON) //sets HTTP headers
                        .content(json)) // ^
                .andExpect(status().isCreated())  //checking response code is 201
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))   //checks response
                .andExpect(jsonPath("$.title").value("Inception")) //checks title using JsonPath
                .andExpect(jsonPath("$.availableCopies").value(3)); //checks dvd count using JsonPath
    }

    @Test
    void returnMoviesWithPagination() throws Exception {
        //endpoint unit test
        //mock data

        MovieResponseDto movie1 = new MovieResponseDto(UUID.randomUUID(), "Inception", 2010, MovieGenre.SCIFI, MovieRating.PG13, 180, 3);
        MovieResponseDto movie2 = new MovieResponseDto(UUID.randomUUID(), "The Matrix", 1999, MovieGenre.SCIFI, MovieRating.PG13, 120, 3);

        Page<MovieResponseDto> page = new PageImpl<>(
                List.of(movie1, movie2),
                PageRequest.of(0, 20),
                42  // pretend there are 42 total movies
        );

        when(movieService.getAllMovies(any())).thenReturn(page);

        //get
        mockMvc.perform(get("/api/movie")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())   // ← keep this the first time so you see the actual JSON
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // page metadata check
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.totalElements").value(42))
                .andExpect(jsonPath("$.totalPages").value(3))

                // first movie check
                .andExpect(jsonPath("$.content[0].title").value("Inception"))
                .andExpect(jsonPath("$.content[0].releaseYear").value(2010))
                .andExpect(jsonPath("$.content[0].movieGenre").value("SCIFI"))
                .andExpect(jsonPath("$.content[0].movieRating").value("PG13"))
                .andExpect(jsonPath("$.content[0].runtime").value(180))
                .andExpect(jsonPath("$.content[0].availableCopies").value(3))   // this matches your 3

                // second movie check
                .andExpect(jsonPath("$.content[1].title").value("The Matrix"))
                .andExpect(jsonPath("$.content[1].releaseYear").value(1999))
                .andExpect(jsonPath("$.content[1].runtime").value(120))
                .andExpect(jsonPath("$.content[1].availableCopies").value(3));
    }
}