package com.laioffer.tripplanner.model;

public record POISearchDto(
        Long id,
        String name,
        String type,
        java.math.BigDecimal rating,
        Double lat,
        Double lng,
        String address
) {
}

