package com.example.fitnessworkouttracker.workout;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserIdOrderByPerformedDateDesc(Long userId);

    List<Workout> findByUserIdAndStatus(Long userId, WorkoutStatus status);

    Optional<Workout> findByIdAndUserId(Long id, Long userId);
}
