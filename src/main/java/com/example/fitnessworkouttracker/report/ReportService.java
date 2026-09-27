package com.example.fitnessworkouttracker.report;

import com.example.fitnessworkouttracker.workout.Workout;
import com.example.fitnessworkouttracker.workout.WorkoutRepository;
import com.example.fitnessworkouttracker.workout.WorkoutStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportService {
    // I'm putting my report math here so my controller stays thin
    private final WorkoutRepository workoutRepository;

    public ReportService(WorkoutRepository workoutRepository) { // I'm injecting workouts to summarize them
        this.workoutRepository = workoutRepository;
    }

    @Transactional(readOnly = true) // I learned readOnly is faster and safer for just reading
    public ProgressReportResponse getProgressReport(Long userId) {
        List<Workout> completed = // I only count COMPLETED workouts for my totals
                workoutRepository.findByUserIdAndStatus(userId, WorkoutStatus.COMPLETED);

        long totalSets = 0;
        long totalReps = 0;
        double totalVolumeKg = 0.0;

        for (Workout w : completed) {
            for (var item : w.getExercises()) { // I'm looping every set entry in my completed workouts
                totalSets += item.getSets(); // I'm adding up my sets
                totalReps += (long) item.getSets() * item.getReps(); // I'm computing my total reps as sets * reps
                double weight = item.getWeightKg() != null ? item.getWeightKg() : 0.0; // I treat my null weight as 0 (bodyweight)
                totalVolumeKg += (double) item.getSets() * item.getReps() * weight; // I learned totalVolume = sets * reps * weight
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
