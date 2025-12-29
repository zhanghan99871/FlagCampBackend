package com.laioffer.tripplanner.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("itineraries")
public record ItineraryEntity(
        @Id Long id,
        Long userId,
        Long cityId,
        String title,
        Short durationDays,
        Long currentVersionId,
        Instant createdAt,
        Instant updatedAt
) {
}

