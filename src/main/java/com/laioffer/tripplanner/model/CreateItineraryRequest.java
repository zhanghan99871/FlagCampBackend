package com.laioffer.tripplanner.model;

public record CreateItineraryRequest(
        Long cityId,
        String title,
        Integer durationDays
) {
}