package com.example.fitnessworkouttracker.workout.dto;

// I'm using this DTO as one line inside my WorkoutResponse, I include the exercise name so the frontend doesn't need a second call
public record WorkoutExerciseResponse(
        Long id,
        Long exerciseId,
        String exerciseName,
        int sets,
        int reps,
        Double weightKg
) {
}
