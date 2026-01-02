package com.laioffer.tripplanner.model;

import java.util.List;

public record POISearchResponse(
        Boolean success,
        List<POISearchDto> data,
        String error
) {
}

