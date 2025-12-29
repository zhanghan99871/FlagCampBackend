package com.laioffer.tripplanner.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("users")
public record UserEntityRecord(
        @Id Long id,
        String email,
        String passwordHash,
        String displayName,
        String avatarUrl,
        Instant createdAt,
        Instant updatedAt
) {
}

