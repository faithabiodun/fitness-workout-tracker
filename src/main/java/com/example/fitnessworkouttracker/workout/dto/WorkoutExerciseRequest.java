package com.example.fitnessworkouttracker.workout.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WorkoutExerciseRequest(
        // I'm asking the frontend which exercise definition this line refers to
        @NotNull Long exerciseId,
        // I'm asking for at least 1 set so I never save an empty line
        @Min(1) int sets,
        // I'm asking for at least 1 rep per set
        @Min(1) int reps,
        // I'm letting weight be optional (bodyweight moves), but if sent it can't be negative
        @DecimalMin("0.0") Double weightKg
) {
}
