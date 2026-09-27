package com.example.fitnessworkouttracker.workout;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    // I'm listing one user's workouts newest first for my history page
    List<Workout> findByUserIdOrderByPerformedDateDesc(Long userId);

    // I'm filtering one user's workouts by their status (planned / completed / skipped)
    List<Workout> findByUserIdAndStatus(Long userId, WorkoutStatus status);

    // I'm fetching one workout only if it belongs to this user so I never leak other users' data
    Optional<Workout> findByIdAndUserId(Long id, Long userId);
}
