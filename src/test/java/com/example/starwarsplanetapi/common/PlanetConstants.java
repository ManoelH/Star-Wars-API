package com.example.starwarsplanetapi.common;

import com.example.starwarsplanetapi.domain.Planet;
import java.util.List;

public class PlanetConstants {

    public static Planet PLANET = new Planet("NAME", "CLIMATE", "TERRAIN");

    public static Planet INVALID_PLANET = new Planet("", "", "");

    public static Planet PLANET_FOUND = new Planet(2L, "NAME", "CLIMATE", "TERRAIN");

    public static List<Planet> PLANETS_FOUND_LIST = List.of(new Planet("MART", "STABLE", "ARID"));
}
