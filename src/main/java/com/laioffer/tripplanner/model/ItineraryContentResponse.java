package com.laioffer.tripplanner.model;

public record ItineraryContentResponse(
        boolean success,
        ItineraryContentDTO data,
        String error
) {}