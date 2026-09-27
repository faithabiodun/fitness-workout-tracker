package com.example.fitnessworkouttracker.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // I learned @RestControllerAdvice turns my exceptions into nice JSON errors everywhere
    private ResponseEntity<ApiError> body(HttpStatus status, String message, HttpServletRequest req) {
        return ResponseEntity.status(status).body( // I'm building my ApiError with timestamp, status, message and path
                new ApiError(Instant.now(), status.value(), message, req.getRequestURI())
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class) // I'm mapping my missing-resource case to 404
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return body(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler({DuplicateResourceException.class, IllegalArgumentException.class}) // I'm grouping my bad-input cases here
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex, HttpServletRequest req) {
        // Duplicate email -> 409, other illegal args -> 400.
        // Keep it simple for beginners: duplicates are conflicts.
        // I learned duplicates are 409 conflicts while other bad args are 400
        HttpStatus status = ex instanceof DuplicateResourceException
                ? HttpStatus.CONFLICT
                : HttpStatus.BAD_REQUEST;
        return body(status, ex.getMessage(), req);
    }

    @ExceptionHandler(BadCredentialsException.class) // I'm turning my bad login into a 401
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        return body(HttpStatus.UNAUTHORIZED, ex.getMessage(), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // I'm joining my @Valid field errors into one message
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return body(HttpStatus.BAD_REQUEST, message, req);
    }

    @ExceptionHandler(Exception.class) // I'm catching everything else as a 500 so I never leak a stack trace
    public ResponseEntity<ApiError> handleOther(Exception ex, HttpServletRequest req) {
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req);
    }
}
