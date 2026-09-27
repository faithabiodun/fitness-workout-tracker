package com.example.fitnessworkouttracker.exception;

public class ResourceNotFoundException extends RuntimeException {
    // I'm throwing this when my lookup misses so my handler returns a 404 JSON
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
