package com.example.starwarsplanetapi.repository;

import com.example.starwarsplanetapi.domain.Planet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;


import java.util.Optional;

import static com.example.starwarsplanetapi.common.PlanetConstants.PLANET;


@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlanetRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlanetRepository planetRepository;

    @AfterEach
    public void afterEachTest(){
        PLANET.setId(null);
    }

    //@BeforeEach

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        Planet planet = planetRepository.save(PLANET);

        Planet sut = testEntityManager.find(Planet.class, planet.getId());

        Assertions.assertThat(sut.getId()).isNotNull();
        Assertions.assertThat(sut).isEqualTo(planet);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        Assertions.assertThatThrownBy(() -> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> planetRepository.save(invalidPlanet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createPlanet_WithExistingName_ThrowsException() {

        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);

        Planet planet2 = new Planet("NAME", "a", "");

        Assertions.assertThatThrownBy(() -> planetRepository.save(planet2)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findPlanetById_WithExistingId_ReturnsPlanet() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);

        Optional<Planet> sut = planetRepository.findById(planet.getId());

        Assertions.assertThat(sut).isNotEmpty();
        Assertions.assertThat(sut.get()).isEqualTo(planet);
    }

    @Test
    public void findPlanetById_WithInvalidId_ReturnsEmpty() {

        Optional<Planet> sut = planetRepository.findById(0L);

        Assertions.assertThat(sut).isEmpty();
    }
}
