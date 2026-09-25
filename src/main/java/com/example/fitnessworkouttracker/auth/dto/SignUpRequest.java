package com.example.fitnessworkouttracker.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public record SignUpRequest(@NotBlank
                            @Size(max=100)
                            String name,

                            @NotBlank
                            @Email
                            String email,

                            @NotBlank
                            @Size(min=8, max=72)
                            String password) {
}
