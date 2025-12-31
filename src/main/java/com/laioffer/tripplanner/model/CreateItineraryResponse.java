package com.laioffer.tripplanner.model;

public record CreateItineraryResponse(
        Boolean success,
        CreateItineraryData data,
        String error
) {
}