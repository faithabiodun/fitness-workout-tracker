package com.example.fitnessworkouttracker.workout.dto;

public record WorkoutExerciseResponse(
        Long id,
        Long exerciseId,
        String exerciseName,
        int sets,
        int reps,
        Double weightKg
) {
}
