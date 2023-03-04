package com.example.starwarsplanetapi.shared;

public interface URLS {

    interface PLANETS {
         String PLANETS = "/planets";
         String FIND_BY_CLIMATE_OR_TERRAIN = "/find-by-climate-or-terrain";
    }

    interface COMMON_FILTERS {
        String FIND_BY_NAME = "/name";
    }
}
