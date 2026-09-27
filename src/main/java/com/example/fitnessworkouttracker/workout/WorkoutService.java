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

    @Transactional(readOnly = true)
    public List<WorkoutResponse> listWorkouts(Long userId) {
        return workoutRepository.findByUserIdOrderByPerformedDateDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkoutResponse getWorkout(Long userId, Long workoutId) {
        return toResponse(findOwned(userId, workoutId));
    }

    @Transactional
    public WorkoutResponse updateStatus(Long userId, Long workoutId, WorkoutStatus status) {
        Workout workout = findOwned(userId, workoutId);
        workout.setStatus(status);
        return toResponse(workoutRepository.save(workout));
    }

    @Transactional
    public void deleteWorkout(Long userId, Long workoutId) {
        workoutRepository.delete(findOwned(userId, workoutId));
    }

    private Workout findOwned(Long userId, Long workoutId) {
        return workoutRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout not found: " + workoutId));
    }

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
