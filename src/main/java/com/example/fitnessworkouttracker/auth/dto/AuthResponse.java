package com.example.fitnessworkouttracker.auth.dto;

public record AuthResponse(
        String token,
        String tokenType
) {
}
