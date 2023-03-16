package com.example.starwarsplanetapi.shared;

public interface URLS {

    interface PLANETS {
         String URI_PLANETS = "/planets";
         String URI_FIND_BY_CLIMATE_OR_TERRAIN = "/find-by-climate-or-terrain";
    }

    interface COMMON_FILTERS {
        String URI_FIND_BY_NAME = "/name";
    }
}
