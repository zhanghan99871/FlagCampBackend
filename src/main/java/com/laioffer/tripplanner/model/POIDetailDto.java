package com.laioffer.tripplanner.model;

import java.util.Map;

public record POIDetailDto(
        Long id,
        String name,
        String type,
        java.math.BigDecimal rating,
        Double lat,
        Double lng,
        Map<String, Object> extra
) {
}

