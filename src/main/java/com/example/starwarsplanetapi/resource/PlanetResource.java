package com.example.starwarsplanetapi.resource;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.service.impl.PlanetServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static com.example.starwarsplanetapi.shared.URLS.PLANETS.PLANETS;

@RestController
@RequestMapping(PLANETS)
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
}
