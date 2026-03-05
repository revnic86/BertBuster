package com.rob.bertbuster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rob.bertbuster.domain.entity.MovieGenre;
import com.rob.bertbuster.domain.entity.MovieRating;
import com.rob.bertbuster.domain.entity.dto.AddMovieDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class MovieControllerTest {

    //integration test with mockMvc

    private MockMvc mockMvc; //fake http client

    @Autowired
    private WebApplicationContext context;  //inject real web context of app

    private final ObjectMapper objectMapper = new ObjectMapper();  //converts dto from object to json later on

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();  // initializes mockMvc
    }

    @Test
    void createMovie_validInput_returns201AndResponse() throws Exception {
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
}