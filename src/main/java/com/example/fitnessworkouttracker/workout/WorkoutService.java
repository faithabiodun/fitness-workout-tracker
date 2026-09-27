package com.example.fitnessworkouttracker.workout;

import com.example.fitnessworkouttracker.exception.ResourceNotFoundException;
import com.example.fitnessworkouttracker.exercise.Exercise;
import com.example.fitnessworkouttracker.exercise.ExerciseRepository;
import com.example.fitnessworkouttracker.repository.UserRepository;
import com.example.fitnessworkouttracker.user.AppUser;
import com.example.fitnessworkouttracker.workout.dto.WorkoutExerciseRequest;
import com.example.fitnessworkouttracker.workout.dto.WorkoutExerciseResponse;
import com.example.fitnessworkouttracker.workout.dto.WorkoutRequest;
import com.example.fitnessworkouttracker.workout.dto.WorkoutResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// I'm putting all my workout business logic here so my controller stays thin
@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    public WorkoutService(WorkoutRepository workoutRepository,
                          ExerciseRepository exerciseRepository,
                          UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    // I'm creating a workout: I load my user, default missing status to COMPLETED, then attach each exercise line
    @Transactional
    public WorkoutResponse createWorkout(Long userId, WorkoutRequest request) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        WorkoutStatus status = request.status() != null ? request.status() : WorkoutStatus.COMPLETED;
        Workout workout = new Workout(user, request.performedDate(), status, request.notes());

        for (WorkoutExerciseRequest item : request.exercises()) {
            Exercise exercise = exerciseRepository.findById(item.exerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Exercise not found: " + item.exerciseId()));
            workout.addExercise(new WorkoutExercise(
                    exercise, item.sets(), item.reps(), item.weightKg()));
        }

        return toResponse(workoutRepository.save(workout));
    }

    // I'm listing one user's workouts newest first and converting each to what I send the frontend
    @Transactional(readOnly = true)
    public List<WorkoutResponse> listWorkouts(Long userId) {
        return workoutRepository.findByUserIdOrderByPerformedDateDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    // I'm getting one workout but only if it belongs to this user
    @Transactional(readOnly = true)
    public WorkoutResponse getWorkout(Long userId, Long workoutId) {
        return toResponse(findOwned(userId, workoutId));
    }

    // I'm updating just my status field (planned / completed / skipped)
    @Transactional
    public WorkoutResponse updateStatus(Long userId, Long workoutId, WorkoutStatus status) {
        Workout workout = findOwned(userId, workoutId);
        workout.setStatus(status);
        return toResponse(workoutRepository.save(workout));
    }

    // I'm deleting a workout, my cascade + orphanRemoval removes its exercise lines too
    @Transactional
    public void deleteWorkout(Long userId, Long workoutId) {
        workoutRepository.delete(findOwned(userId, workoutId));
    }

    // I'm reusing this check so every get/update/delete only touches the owner's own workout
    private Workout findOwned(Long userId, Long workoutId) {
        return workoutRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout not found: " + workoutId));
    }

    // I'm converting my entity into what I send back so I never expose my AppUser directly
    private WorkoutResponse toResponse(Workout workout) {
        List<WorkoutExerciseResponse> items = workout.getExercises().stream()
                .map(e -> new WorkoutExerciseResponse(
                        e.getId(),
                        e.getExercise().getId(),
                        e.getExercise().getName(),
                        e.getSets(),
                        e.getReps(),
                        e.getWeightKg()))
                .toList();
        return new WorkoutResponse(
                workout.getId(),
                workout.getPerformedDate(),
                workout.getStatus(),
                workout.getNotes(),
                items);
    }
}
