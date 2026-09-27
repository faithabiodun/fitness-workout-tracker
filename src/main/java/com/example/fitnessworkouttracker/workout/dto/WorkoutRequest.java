package com.example.fitnessworkouttracker.workout.dto;

import com.example.fitnessworkouttracker.workout.WorkoutStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record WorkoutRequest(
        // I'm asking the frontend to always send me the date the workout happened
        @NotNull LocalDate performedDate,
        // I'm letting status be optional here, I'll default it to COMPLETED in my service
        WorkoutStatus status,
        // I'm letting the frontend send me optional free-text notes
        String notes,
        // I'm asking for at least one exercise, each one is validated too
        @NotEmpty List<@Valid WorkoutExerciseRequest> exercises
) {
}
