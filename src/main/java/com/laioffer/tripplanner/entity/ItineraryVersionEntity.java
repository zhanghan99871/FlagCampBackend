package com.laioffer.tripplanner.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("itinerary_versions")
public record ItineraryVersionEntity(
        @Id Long id,
        Long itineraryId,
        Integer versionNo,
        String name,
        Boolean isPublished,
        String data, // JSONB stored as String, can be parsed to Map/Object when needed
        Instant createdAt,
        Instant updatedAt
) {
}

