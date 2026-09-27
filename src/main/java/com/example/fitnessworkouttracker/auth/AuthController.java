// I'm in my auth package where my login/signup endpoints live
package com.example.fitnessworkouttracker.auth;

import com.example.fitnessworkouttracker.auth.dto.AuthResponse;
import com.example.fitnessworkouttracker.auth.dto.LoginRequest;
import com.example.fitnessworkouttracker.auth.dto.SignUpRequest;
// I learned @Valid triggers my dto validation rules automatically
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
// I learned these map my HTTP routes and JSON bodies
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// I learned @RestController means my methods return JSON
// I learned @RequestMapping sets my base path to /api/auth
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // I'm holding my service that does my real auth work
    private final AuthService authService;

    // I'm injecting my AuthService via my constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // I learned @PostMapping handles my POST /api/auth/signup
    // I learned @ResponseStatus 201 means I created something
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    // I'm taking my validated signup JSON and returning my token
    public AuthResponse signup(@Valid @RequestBody SignUpRequest request) {
        return authService.signup(request);
    }

    // I learned @PostMapping handles my POST /api/auth/login
    // I'm taking my validated login JSON and returning my token
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
