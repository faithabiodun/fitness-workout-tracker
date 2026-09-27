package com.example.fitnessworkouttracker.report;

import com.example.fitnessworkouttracker.workout.Workout;
import com.example.fitnessworkouttracker.workout.WorkoutRepository;
import com.example.fitnessworkouttracker.workout.WorkoutStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportService {

    private final WorkoutRepository workoutRepository;

    public ReportService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Transactional(readOnly = true)
    public ProgressReportResponse getProgressReport(Long userId) {
        List<Workout> completed =
                workoutRepository.findByUserIdAndStatus(userId, WorkoutStatus.COMPLETED);

        long totalSets = 0;
        long totalReps = 0;
        double totalVolumeKg = 0.0;

        for (Workout w : completed) {
            for (var item : w.getExercises()) {
                totalSets += item.getSets();
                totalReps += (long) item.getSets() * item.getReps();
                double weight = item.getWeightKg() != null ? item.getWeightKg() : 0.0;
                totalVolumeKg += (double) item.getSets() * item.getReps() * weight;
            }
        }

        long totalWorkouts = workoutRepository.findByUserIdOrderByPerformedDateDesc(userId).size();

        return new ProgressReportResponse(
                totalWorkouts,
                completed.size(),
                totalSets,
                totalReps,
                totalVolumeKg);
    }
}
