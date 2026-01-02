package com.laioffer.tripplanner.model;

public record POIDetailResponse(
        Boolean success,
        POIDetailDto data,
        String error
) {
}

