package com.example.fitnessworkouttracker.exercise;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    // I'm exposing my exercises as REST JSON under /api/exercises
    private final ExerciseRepository exerciseRepository;

    public ExerciseController(ExerciseRepository exerciseRepository) { // I'm using constructor injection for my repo
        this.exerciseRepository = exerciseRepository;
    }

    // GET /api/exercises -> public (see SecurityConfig)
    // I'm listing everything because SecurityConfig sets this path to permitAll
    @GetMapping
    public List<Exercise> list() {
        return exerciseRepository.findAll();
    }

    // GET /api/exercises/{id} -> public
    @GetMapping("/{id}")
    public Exercise getOne(@PathVariable Long id) {
        // I throw my ResourceNotFoundException so I get a nice 404 JSON
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new com.example.fitnessworkouttracker.exception.ResourceNotFoundException(
                        "Exercise not found: " + id));
    }
}
