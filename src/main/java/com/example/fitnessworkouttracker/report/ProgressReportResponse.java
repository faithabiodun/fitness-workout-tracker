package com.example.fitnessworkouttracker.report;

public record ProgressReportResponse(
        long totalWorkouts,
        long completedWorkouts,
        long totalSets,
        long totalReps,
        double totalVolumeKg
) {
}
