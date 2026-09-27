// I'm in the auth package where my login/signup logic lives
package com.example.fitnessworkouttracker.auth.dto;

// I learned @NotBlank means this field can't be empty
// I learned @Size just limits how long my name can be
// I learned @Email checks the email format for me
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

// I'm using a record so my signup input is just name + email + password
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
