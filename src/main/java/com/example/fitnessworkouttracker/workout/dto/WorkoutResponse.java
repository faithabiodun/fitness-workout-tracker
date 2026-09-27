package com.example.fitnessworkouttracker.workout.dto;

import com.example.fitnessworkouttracker.workout.WorkoutStatus;

import java.time.LocalDate;
import java.util.List;

public record WorkoutResponse(
        Long id,
        LocalDate performedDate,
        WorkoutStatus status,
        String notes,
        List<WorkoutExerciseResponse> exercises
) {
}
