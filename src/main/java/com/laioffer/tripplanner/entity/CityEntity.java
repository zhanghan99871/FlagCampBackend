package com.laioffer.tripplanner.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("cities")
public record CityEntity(
        @Id Long id,
        String name,
        String country,
        Double lat,
        Double lng,
        Instant createdAt,
        Instant updatedAt
) {
}

