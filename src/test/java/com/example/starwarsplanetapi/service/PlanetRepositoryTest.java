package com.example.starwarsplanetapi.service;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.repository.PlanetRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;


import static com.example.starwarsplanetapi.common.PlanetConstants.PLANET;


@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlanetRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlanetRepository planetRepository;


    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        Planet planet = planetRepository.save(PLANET);

        Planet sut = testEntityManager.find(Planet.class, planet.getId());

        Assertions.assertThat(sut.getId()).isNotNull();
        Assertions.assertThat(sut).isEqualTo(planet);
    }
}
