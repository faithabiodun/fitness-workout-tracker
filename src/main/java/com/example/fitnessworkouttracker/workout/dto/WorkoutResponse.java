package com.example.fitnessworkouttracker.workout.dto;

import com.example.fitnessworkouttracker.workout.WorkoutStatus;

import java.time.LocalDate;
import java.util.List;

// I'm using this DTO as what I send back to the frontend after create/list/get/update
public record WorkoutResponse(
        Long id,
        LocalDate performedDate,
        WorkoutStatus status,
        String notes,
        List<WorkoutExerciseResponse> exercises
) {
}
