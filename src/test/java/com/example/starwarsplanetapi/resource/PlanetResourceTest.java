package com.example.starwarsplanetapi.resource;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.service.impl.PlanetServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static com.example.starwarsplanetapi.common.PlanetConstants.PLANET;
import static com.example.starwarsplanetapi.shared.URLS.COMMON_FILTERS.URI_FIND_BY_NAME;
import static com.example.starwarsplanetapi.shared.URLS.PLANETS.URI_PLANETS;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(PlanetResource.class)
public class PlanetResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlanetServiceImpl planetServiceImpl;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated() throws Exception {
      Mockito.when(planetServiceImpl.create(PLANET)).thenReturn(PLANET);

      mockMvc.perform(MockMvcRequestBuilders.post(URI_PLANETS).content(objectMapper.writeValueAsString(PLANET))
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(MockMvcResultMatchers.status().isCreated())
              .andExpect(MockMvcResultMatchers.jsonPath("$").value(PLANET));
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnsBadRequest() throws Exception {

        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        mockMvc.perform(MockMvcRequestBuilders.post(URI_PLANETS).content(objectMapper.writeValueAsString(emptyPlanet))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

        mockMvc.perform(MockMvcRequestBuilders.post(URI_PLANETS).content(objectMapper.writeValueAsString(invalidPlanet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistentName_ReturnsConflict() throws Exception {

        Mockito.when(planetServiceImpl.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(URI_PLANETS).content(objectMapper.writeValueAsString(PLANET))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());

    }


    @Test
    public void findPlanetById_WithExistentId_ReturnsPlanet() throws Exception {

        Mockito.when(planetServiceImpl.findById(any())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(MockMvcRequestBuilders.get(URI_PLANETS+"/"+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(PLANET));

    }

    @Test
    public void findPlanetById_WithNonexistentId_ReturnsNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(URI_PLANETS+"/"+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void findPlanetByName_WithExistentName_ReturnsListOfPlanets() throws Exception {

        Mockito.when(planetServiceImpl.findPlanetByName("NAME")).thenReturn(Optional.of(List.of(PLANET)));

        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIND_BY_NAME).content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Optional.of(List.of(PLANET))));
    }

    @Test
    public void findPlanetByName_WithNoneexistentName_ReturnsListNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIND_BY_NAME).content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
