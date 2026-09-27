package com.example.fitnessworkouttracker.exception;

public class DuplicateResourceException extends RuntimeException {
    // I'm throwing this on duplicates (like email) so my handler returns a 409 JSON
    public DuplicateResourceException(String message) {
        super(message);
    }
}
