package com.example.starwarsplanetapi.resource;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.service.impl.PlanetServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static com.example.starwarsplanetapi.common.PlanetConstants.PLANET;
import static com.example.starwarsplanetapi.shared.URLS.COMMON_FILTERS.URI_FIND_BY_NAME;
import static com.example.starwarsplanetapi.shared.URLS.PLANETS.URI_FIND_BY_CLIMATE_OR_TERRAIN;
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

        mockMvc.perform(MockMvcRequestBuilders.post(URI_PLANETS+URI_FIND_BY_NAME).content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{'id':null,'name':'NAME', 'climate':'CLIMATE', 'terrain': TERRAIN}]"));
                //.andExpect(MockMvcResultMatchers.jsonPath("$.[*]").value(List.of(PLANET)));
    }

    @Test
    public void findPlanetByName_WithNoneExistentName_ReturnsListNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_PLANETS+URI_FIND_BY_NAME).content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void findPlanetByFilter_WithValidValues_ReturnsListOfPlanets() throws Exception {

        Mockito.when(planetServiceImpl.findPlanetByClimateOrTerrain(new Planet())).thenReturn(Optional.of(List.of(PLANET)));

        Mockito.when(planetServiceImpl.findPlanetByClimateOrTerrain(PLANET)).thenReturn(Optional.of(List.of(PLANET)));

        mockMvc.perform(MockMvcRequestBuilders.post(URI_PLANETS+URI_FIND_BY_CLIMATE_OR_TERRAIN).content(objectMapper.writeValueAsString(new Planet()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));

        mockMvc.perform(MockMvcRequestBuilders.post(URI_PLANETS+URI_FIND_BY_CLIMATE_OR_TERRAIN).content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{'id':null,'name':'NAME', 'climate':'CLIMATE', 'terrain': TERRAIN}]"));
        //.andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(List.of(PLANET)));
    }

    @Test
    public void findPlanetByFilter_WithInvalidValues_ReturnsNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_PLANETS+URI_FIND_BY_CLIMATE_OR_TERRAIN).content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        //.andExpect(MockMvcResultMatchers.jsonPath("$.[*]").value(List.of(PLANET)));
    }

    @Test
    public void deletePlanet_WithExistentId_ReturnsStatusNoContent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete(URI_PLANETS+"/"+3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deletePlanet_WithExistentId_ThrowsNot() throws Exception {

        final Long id = 1L;

        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(planetServiceImpl).deletePlanetById(id);

        mockMvc.perform(MockMvcRequestBuilders.delete(URI_PLANETS+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
