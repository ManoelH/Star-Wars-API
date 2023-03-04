package com.example.starwarsplanetapi.repository;

import com.example.starwarsplanetapi.domain.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long>, QueryByExampleExecutor<Planet> {

    Optional<List<Planet>> findPlanetByNameContains(String name);
}
