package com.example.fitnessworkouttracker.workout.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WorkoutExerciseRequest(
        @NotNull Long exerciseId,
        @Min(1) int sets,
        @Min(1) int reps,
        @DecimalMin("0.0") Double weightKg
) {
}
