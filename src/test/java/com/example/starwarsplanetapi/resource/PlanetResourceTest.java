package com.example.starwarsplanetapi.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.example.starwarsplanetapi.common.PlanetConstants.PLANET;
import static com.example.starwarsplanetapi.shared.URLS.PLANETS.URI_PLANETS;

@WebMvcTest(PlanetResource.class)
public class PlanetResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated() throws Exception {

      mockMvc.perform(MockMvcRequestBuilders.post(URI_PLANETS).content(objectMapper.writeValueAsString(PLANET)))
              .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
