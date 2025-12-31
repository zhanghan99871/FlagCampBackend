package com.laioffer.tripplanner.model;

public record ItineraryData(
        Long id,
        String title,
        Integer durationDays,
        Long cityId
) {
}