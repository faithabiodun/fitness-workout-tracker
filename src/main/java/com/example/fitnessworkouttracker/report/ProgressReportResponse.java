package com.example.fitnessworkouttracker.report;

public record ProgressReportResponse(
        // I'm using a record so I get my JSON response object without boilerplate
        long totalWorkouts, // my count of all workouts
        long completedWorkouts, // my count of only COMPLETED ones
        long totalSets, // I'm summing every set I finished
        long totalReps, // I'm summing sets * reps
        double totalVolumeKg // I learned this is my sets * reps * weight in kg
) {
}
