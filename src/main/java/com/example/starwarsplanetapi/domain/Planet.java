package com.example.starwarsplanetapi.domain;

import com.example.starwarsplanetapi.jacoco.ExcludeFromJacocoGeneratedReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Data
@Entity(name = "planets")
@NoArgsConstructor
@AllArgsConstructor
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty
    private String name;

    @Column(nullable = false)
    @NotEmpty
    private String climate;

    @Column(nullable = false)
    @NotEmpty
    private String terrain;

    public Planet(String name, String climate, String terrain) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(o, this);
    }

    @Override
    @ExcludeFromJacocoGeneratedReport
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", climate='" + climate + '\'' +
                ", terrain='" + terrain + '\'' +
                '}';
    }
}
