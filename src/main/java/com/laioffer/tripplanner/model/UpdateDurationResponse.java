package com.laioffer.tripplanner.model;

public record UpdateDurationResponse(
        Boolean success,
        String data,
        String error
) {
}