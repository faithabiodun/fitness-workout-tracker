package com.example.fitnessworkouttracker.exercise;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseRepository exerciseRepository;

    public ExerciseController(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    // GET /api/exercises -> public (see SecurityConfig)
    @GetMapping
    public List<Exercise> list() {
        return exerciseRepository.findAll();
    }

    // GET /api/exercises/{id} -> public
    @GetMapping("/{id}")
    public Exercise getOne(@PathVariable Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new com.example.fitnessworkouttracker.exception.ResourceNotFoundException(
                        "Exercise not found: " + id));
    }
}
