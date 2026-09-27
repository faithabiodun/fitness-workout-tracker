// I'm in my dto package, this is what I send back after auth
package com.example.fitnessworkouttracker.auth.dto;

// I'm using a record for my auth output (my JWT + its type like Bearer)
public record AuthResponse(
        // I'm storing my JWT string here
        String token,
        // I'm storing "Bearer" here so my frontend knows how to use it
        String tokenType
) {
}
