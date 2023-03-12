package com.example.starwarsplanetapi.service;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.repository.PlanetRepository;
import com.example.starwarsplanetapi.service.impl.PlanetServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.starwarsplanetapi.common.PlanetConstants.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

    //operacap_estado_retorno

    @InjectMocks
    private PlanetServiceImpl planetServiceImpl;

    @Mock
    private PlanetRepository planetRepository;

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {

        //MOCKANDO O REPOSITORY PARA TESTAR APENAS A IMPLEMENTAÇÃO DO SERVICE
        Mockito.when(planetRepository.save(PLANET)).thenReturn(PLANET);

        //SYSTEM UNDER TEST
        Planet sut = planetServiceImpl.create(PLANET);

        Assertions.assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {

        //MOCKANDO O REPOSITORY PARA TESTAR APENAS A IMPLEMENTAÇÃO DO SERVICE
        Mockito.when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        Assertions.assertThatThrownBy(() -> planetServiceImpl.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findPlanetById_WithInvalidId_ReturnsEmpty() {

        Mockito.when(planetRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<Planet> sut = planetServiceImpl.findById(3L);

        Assertions.assertThat(sut).isEmpty();
    }

    @Test
    public void findPlanetById_WithValidId_ReturnsPlanet() {

        Mockito.when(planetRepository.findById(2L)).thenReturn(Optional.of(PLANET_FOUND));

        Optional<Planet> sut = planetServiceImpl.findById(2L);

        Assertions.assertThat(sut).isNotEmpty();
        Assertions.assertThat(sut.get()).isEqualTo(PLANET_FOUND);
    }

    @Test
    public void findPlanetByName_WithNotExistentName_ReturnsEmpty() {

        Mockito.when(planetRepository.findPlanetByNameContains("ZZ")).thenReturn(Optional.empty());

        Optional<List<Planet>> sut = planetServiceImpl.findPlanetByName("ZZ");

        Assertions.assertThat(sut).isEmpty();
    }

    @Test
    public void findPlanetByName_WithExistentName_ReturnsListOfPlanet() {

        Mockito.when(planetRepository.findPlanetByNameContains("AR")).thenReturn(Optional.of(PLANETS_FOUND_LIST));

        Optional<List<Planet>> sut = planetServiceImpl.findPlanetByName("AR");

        Assertions.assertThat(sut).isNotEmpty();
        Assertions.assertThat(sut.get()).isEqualTo(PLANETS_FOUND_LIST);
    }

    @Test
    public void findPlanetByClimateOrTerrain_WithNotExistentFilter_ReturnsEmptyArray() {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("climate", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("terrain", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Planet> example = Example.of(PLANET, matcher);

        Mockito.when(planetRepository.findAll(example)).thenReturn(Collections.emptyList());

        List<Planet> sut = planetServiceImpl.findPlanetByClimateOrTerrain(PLANET);

        Assertions.assertThat(sut).isEqualTo(Collections.emptyList());
    }

    @Test
    public void findPlanetByClimateOrTerrain_WithExistentFilter_ReturnsNotEmptyArray() {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("climate", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("terrain", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Planet> example = Example.of(PLANET_FOUND, matcher);

        Mockito.when(planetRepository.findAll(example)).thenReturn(PLANETS_FOUND_LIST);

        List<Planet> sut = planetServiceImpl.findPlanetByClimateOrTerrain(PLANET_FOUND);

        Assertions.assertThat(sut).isEqualTo(PLANETS_FOUND_LIST);
    }

    @Test
    public void deletePlanetById_WithNotExistentId_ReturnsException() {

        Mockito.doThrow(new RuntimeException()).when(planetRepository).deleteById(any());

        Assertions.assertThatThrownBy(() -> planetServiceImpl.deletePlanetById(any())).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void deletePlanetById_WithExistentId_DoNotReturnsException() {
        Assertions.assertThatCode(() -> planetServiceImpl.deletePlanetById(1L)).doesNotThrowAnyException();
    }
}
