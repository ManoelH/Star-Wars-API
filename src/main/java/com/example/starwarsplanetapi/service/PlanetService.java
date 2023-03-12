package com.example.starwarsplanetapi.service;

import com.example.starwarsplanetapi.domain.Planet;

import java.util.List;
import java.util.Optional;

public interface PlanetService {

    Planet create(Planet planet);
    Optional<Planet> findById(Long id);
    Optional<List<Planet>> findPlanetByName(String name);
    List<Planet> findPlanetByClimateOrTerrain(Planet planet);
    void deletePlanetById(Long id);
}
