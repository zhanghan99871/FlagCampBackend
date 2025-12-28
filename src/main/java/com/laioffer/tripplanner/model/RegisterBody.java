package com.laioffer.tripplanner.model;

public record RegisterBody(
        String username,
        String password,
        String email
) {
}
