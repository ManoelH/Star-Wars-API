package com.example.starwarsplanetapi.it;

import com.example.starwarsplanetapi.domain.Planet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.starwarsplanetapi.common.PlanetConstants.PLANET;
import static com.example.starwarsplanetapi.common.PlanetConstants.PLANET_FOUND_TATOINE;
import static com.example.starwarsplanetapi.shared.URLS.COMMON_FILTERS.URI_FIND_BY_NAME;
import static com.example.starwarsplanetapi.shared.URLS.PLANETS.URI_FIND_BY_CLIMATE_OR_TERRAIN;
import static com.example.starwarsplanetapi.shared.URLS.PLANETS.URI_PLANETS;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/scripts/import_planets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/truncate_planets.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PlanetIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() {

    }

    @Test
    void createPlanet_Succeed_ReturnsCreated() {

        ResponseEntity<Planet> sut = testRestTemplate.postForEntity(URI_PLANETS, PLANET, Planet.class);

        Assertions.assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(Objects.requireNonNull(sut.getBody()).getId()).isNotNull();
        Assertions.assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
        Assertions.assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        Assertions.assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    void findPLanetById_WithValidId_ReturnsPlanet() {

        ResponseEntity<Planet> sut = testRestTemplate.getForEntity(URI_PLANETS+"/1", Planet.class);
        Assertions.assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(sut.getBody()).isEqualTo(PLANET_FOUND_TATOINE);
    }

    @Test
    void findPlanetByName_WithValidName_ReturnsPlanet() {

        Planet planetParameter = new Planet();
        planetParameter.setName(PLANET_FOUND_TATOINE.getName());

        ResponseEntity<Planet[]> sut = testRestTemplate.postForEntity(URI_PLANETS+URI_FIND_BY_NAME, planetParameter, Planet[].class);

        Assertions.assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(sut.getBody()).isNotEmpty();

        List<Planet> sutList = Arrays.asList(Objects.requireNonNull(sut.getBody()));
        Assertions.assertThat(sutList.contains(PLANET_FOUND_TATOINE)).isTrue();
    }

    @Test
    void findPlanetByName_WithEmptyParameters_ReturnsAllPlanets() {

        Planet planetParameter = new Planet("", "", "");

        ResponseEntity<Planet[]> sut = testRestTemplate.postForEntity(URI_PLANETS+URI_FIND_BY_NAME, planetParameter, Planet[].class);

        Assertions.assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(sut.getBody()).isNotEmpty();

        List<Planet> sutList = Arrays.asList(Objects.requireNonNull(sut.getBody()));
        Assertions.assertThat(sutList).hasSize(3);
    }

    @Test
    void findPlanetByClimate_WithValidParameter_ReturnsPlanets() {

        Planet planetParameter = new Planet();
        planetParameter.setClimate(PLANET_FOUND_TATOINE.getClimate());

        ResponseEntity<Planet[]> sut = testRestTemplate.postForEntity(URI_PLANETS+URI_FIND_BY_CLIMATE_OR_TERRAIN, planetParameter, Planet[].class);

        Assertions.assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(sut.getBody()).isNotEmpty();

        List<Planet> sutList = Arrays.asList(Objects.requireNonNull(sut.getBody()));
        Assertions.assertThat(sutList).hasSize(1);
        Assertions.assertThat(sutList.contains(PLANET_FOUND_TATOINE)).isTrue();
    }

    @Test
    void findPlanetByTerrain_WithValidParameter_ReturnsPlanets() {

        Planet planetParameter = new Planet();
        planetParameter.setTerrain(PLANET_FOUND_TATOINE.getTerrain());

        ResponseEntity<Planet[]> sut = testRestTemplate.postForEntity(URI_PLANETS+URI_FIND_BY_CLIMATE_OR_TERRAIN, planetParameter, Planet[].class);

        Assertions.assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(sut.getBody()).isNotEmpty();

        List<Planet> sutList = Arrays.asList(Objects.requireNonNull(sut.getBody()));
        Assertions.assertThat(sutList).hasSize(1);
        Assertions.assertThat(sutList.contains(PLANET_FOUND_TATOINE)).isTrue();
    }

    @Test
    void deletePlanetByid_WithValidId_ReturnsNoContent() {

        testRestTemplate.delete(URI_PLANETS, 1);

        ResponseEntity<Void> sut = testRestTemplate.exchange(URI_PLANETS + "/"+ PLANET_FOUND_TATOINE.getId(), HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
