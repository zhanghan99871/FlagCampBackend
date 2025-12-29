package com.laioffer.tripplanner.model;

public record LoginResponse(
        Boolean success,
        UserData user,
        String token,
        String error
) {
}
