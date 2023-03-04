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
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.starwarsplanetapi.common.PlanetConstants.*;

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

        Mockito.when(planetRepository.findPlanetByClimateContainsOrTerrainContains("ZZ"))
                .thenReturn(Optional.of(new ArrayList<>()));

        Optional<List<Planet>> sut = planetServiceImpl.findPlanetByClimateOrTerrain("ZZ");

        Assertions.assertThat(sut).isEqualTo(Optional.of(new ArrayList<Planet>()));
    }

    @Test
    public void findPlanetByClimateOrTerrain_WithExistentFilter_ReturnsNotEmptyArray() {

        Mockito.when(planetRepository.findPlanetByClimateContainsOrTerrainContains("a"))
                .thenReturn(Optional.of(PLANETS_FOUND_LIST));

        Optional<List<Planet>> sut = planetServiceImpl.findPlanetByClimateOrTerrain("a");

        Assertions.assertThat(sut).isEqualTo(Optional.of(PLANETS_FOUND_LIST));
    }
}
