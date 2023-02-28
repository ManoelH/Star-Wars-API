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

import static com.example.starwarsplanetapi.common.PlanetConstants.INVALID_PLANET;
import static com.example.starwarsplanetapi.common.PlanetConstants.PLANET;

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
}
