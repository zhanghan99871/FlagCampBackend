package com.laioffer.tripplanner.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("pois")
public record POIEntity(
        @Id Long id,
        String provider,
        String providerPoiId,
        Long cityId,
        String name,
        String type,
        java.math.BigDecimal rating,
        Integer ratingCount,
        Short priceLevel,
        String address,
        String phone,
        String website,
        Double lat,
        Double lng,
        String extra, // JSONB stored as String, can be parsed to Map/Object when needed
        Instant createdAt,
        Instant updatedAt
) {
}

