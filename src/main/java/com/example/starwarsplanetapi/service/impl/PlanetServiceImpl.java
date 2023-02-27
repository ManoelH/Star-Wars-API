package com.example.starwarsplanetapi.service.impl;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.repository.PlanetRepository;
import com.example.starwarsplanetapi.service.PlanetService;
import org.springframework.stereotype.Service;

@Service
public class PlanetServiceImpl implements PlanetService {

    private PlanetRepository planetRepository;


    public PlanetServiceImpl(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Override
    public Planet create(Planet planet) {
        return planetRepository.save(planet);
    }
}
