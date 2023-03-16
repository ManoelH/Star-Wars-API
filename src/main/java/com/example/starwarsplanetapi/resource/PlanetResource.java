package com.example.starwarsplanetapi.resource;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.service.impl.PlanetServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.example.starwarsplanetapi.shared.URLS.COMMON_FILTERS.URI_FIND_BY_NAME;
import static com.example.starwarsplanetapi.shared.URLS.PLANETS.URI_FIND_BY_CLIMATE_OR_TERRAIN;
import static com.example.starwarsplanetapi.shared.URLS.PLANETS.URI_PLANETS;

@RestController
@RequestMapping(URI_PLANETS)
@AllArgsConstructor
public class PlanetResource {

    @Autowired
    private PlanetServiceImpl planetServiceImpl;

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody Planet planet) {
        Planet planetCreated = planetServiceImpl.create(planet);
        return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> findById(@PathVariable Long id) {
        return planetServiceImpl.findById(id).map(planet -> ResponseEntity.ok(planet))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(URI_FIND_BY_NAME)
    public ResponseEntity<List<Planet>> findPlanetByName(@RequestBody Planet planet) {
        return planetServiceImpl.findPlanetByName(planet.getName()).map(list -> ResponseEntity.ok(list))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(URI_FIND_BY_CLIMATE_OR_TERRAIN)
    public ResponseEntity<List<Planet>> findPlanetByClimateOrTerrain(@RequestBody Planet planet) {
        List<Planet> list = planetServiceImpl.findPlanetByClimateOrTerrain(planet);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        planetServiceImpl.deletePlanetById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
