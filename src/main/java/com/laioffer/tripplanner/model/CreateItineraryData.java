package com.laioffer.tripplanner.model;

public record CreateItineraryData(
        Long itineraryId,
        Long currentVersionId
) {
}