// I'm in my dto package where my request/response shapes live
package com.example.fitnessworkouttracker.auth.dto;

// I learned these check my login input before it reaches my code
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// I'm using a record for my login input (just email + password)
public record LoginRequest(
        // I learned this makes sure my email isn't blank and looks like an email
        @NotBlank
        @Email
        String email,

        // I learned this forces my password to be 8-72 chars long
        @NotBlank
        @Size(min=8, max=72)
        String password
) {
}
