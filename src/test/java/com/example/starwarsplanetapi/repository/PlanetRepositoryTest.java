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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.jdbc.Sql;


import java.util.List;
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

    @Test
    public void findPlanetByName_WithExistingName_ReturnsListOfPlanet() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);

        Optional<List<Planet>> sut = planetRepository.findPlanetByNameContains(planet.getName());

        Assertions.assertThat(sut).isNotEmpty();
        Assertions.assertThat(sut).isEqualTo(Optional.of(List.of(PLANET)));
    }

    @Test
    public void findPlanetByName_WithInvalidName_ReturnsEmpty() {

        Optional<List<Planet>> sut = planetRepository.findPlanetByNameContains("0");
        Assertions.assertThat(sut.get().isEmpty()).isTrue();
    }

    @Sql(scripts = "/import_planets.sql")
    @Test
    public void findPlanetByFilter_WithExistingValues_ReturnsListOfPlanet() {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("climate", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("terrain", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Planet> exampleFiltering = Example.of(new Planet("TATOINE", "ARID", "DESERT"), matcher);

        List<Planet> sutFiltering = planetRepository.findAll(exampleFiltering);

        Assertions.assertThat(sutFiltering).isNotEmpty();
        Assertions.assertThat(sutFiltering).hasSize(1);

        Example<Planet> exampleGettingAll = Example.of(new Planet(), matcher);
        List<Planet> sutGettingAll = planetRepository.findAll(exampleGettingAll);
        Assertions.assertThat(sutGettingAll).isNotEmpty();
        Assertions.assertThat(sutGettingAll).hasSize(3);
    }

    @Test
    public void findPlanetByFilter_WithInvalidValues_ReturnsEmpty() {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("climate", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("terrain", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Planet> example = Example.of(PLANET, matcher);

        List<Planet> sut = planetRepository.findAll(example);

        Assertions.assertThat(sut).isEmpty();
    }

    @Test
    public void deletePlanetById_WithExistentId_DeletePlanet() {
        Assertions.assertThatThrownBy(() -> planetRepository.deleteById(4L)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Sql(scripts = "/import_planets.sql")
    @Test
    public void deletePlanetById_WithNoneExistentId_ThrowsException() {
        Assertions.assertThatNoException().isThrownBy(() -> planetRepository.deleteById(3L));
    }
}
