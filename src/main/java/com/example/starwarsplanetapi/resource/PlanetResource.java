package com.example.starwarsplanetapi.resource;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.service.impl.PlanetServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
