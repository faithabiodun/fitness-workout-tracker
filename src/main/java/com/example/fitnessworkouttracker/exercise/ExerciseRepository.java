package com.example.fitnessworkouttracker.exercise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    // I get all my CRUD methods for free from JpaRepository
    Optional<Exercise> findByNameIgnoreCase(String name); // I learned this finder ignores case when I check duplicates
}
