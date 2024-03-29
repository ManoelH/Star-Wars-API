package com.example.starwarsplanetapi.service.impl;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.repository.PlanetRepository;
import com.example.starwarsplanetapi.service.PlanetService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetServiceImpl implements PlanetService {

    private final PlanetRepository planetRepository;


    public PlanetServiceImpl(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Override
    public Planet create(Planet planet) {
        planet.setName(planet.getName().toUpperCase());
        planet.setClimate(planet.getClimate().toUpperCase());
        planet.setTerrain(planet.getTerrain().toUpperCase());
        return planetRepository.save(planet);
    }

    @Override
    public Optional<Planet> findById(Long id) {
        return planetRepository.findById(id);
    }

    @Override
    public Optional<List<Planet>> findPlanetByName(String name) {
        return planetRepository.findPlanetByNameContains(name);
    }

    @Override
    public Optional<List<Planet>> findPlanetByClimateOrTerrain(Planet planet) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("climate", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("terrain", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        return Optional.of(planetRepository.findAll(Example.of(planet, matcher)));
    }

    @Override
    public void deletePlanetById(Long id) {
        planetRepository.deleteById(id);
    }
}
