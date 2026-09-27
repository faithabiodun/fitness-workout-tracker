package com.example.fitnessworkouttracker.exercise;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExerciseSeeder implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;

    public ExerciseSeeder(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) {
        if (exerciseRepository.count() > 0) {
            return;
        }
        List<Exercise> defaults = List.of(
                new Exercise("Push-up", "Chest", "Bodyweight chest exercise"),
                new Exercise("Squat", "Legs", "Bodyweight or barbell squat"),
                new Exercise("Pull-up", "Back", "Bodyweight back exercise"),
                new Exercise("Bench Press", "Chest", "Barbell bench press"),
                new Exercise("Deadlift", "Back", "Barbell deadlift"),
                new Exercise("Overhead Press", "Shoulders", "Barbell or dumbbell press"),
                new Exercise("Barbell Row", "Back", "Bent-over barbell row"),
                new Exercise("Plank", "Core", "Core hold for time"),
                new Exercise("Lunge", "Legs", "Dumbbell or bodyweight lunge"),
                new Exercise("Bicep Curl", "Arms", "Dumbbell curl")
        );
        exerciseRepository.saveAll(defaults);
    }
}
