package com.example.starwarsplanetapi.common;

import com.example.starwarsplanetapi.domain.Planet;

public class PlanetConstants {

    public static Planet PLANET = new Planet("Name", "Climate", "Terrain");

    public static Planet INVALID_PLANET = new Planet("", "", "");
}
