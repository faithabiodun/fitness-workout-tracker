package com.example.fitnessworkouttracker.exception;

import java.time.Instant;

public record ApiError(
        // I'm shaping my error JSON with this record so every error looks the same
        Instant timestamp, // I'm stamping when my error happened
        int status, // my HTTP status code like 404 or 409
        String message, // I'm putting my human-readable reason here
        String path // I'm recording which URL caused my error
) {
}
