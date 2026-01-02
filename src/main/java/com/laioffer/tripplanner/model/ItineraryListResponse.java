package com.laioffer.tripplanner.model;

import java.util.List;

public record ItineraryListResponse(
        Boolean success,
        List<ItineraryData> data,
        String error
) {
}