package com.laioffer.tripplanner.model;

public record RegisterResponse (
        Boolean success,
        UserData user,
        String error
) {
}
