package com.example.fitnessworkouttracker.workout;

import com.example.fitnessworkouttracker.exercise.Exercise;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "workout_exercises")
public class WorkoutExercise {

    // I'm using this as my auto-generated primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // I'm linking back to my parent workout (many rows belong to one workout)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    // I'm linking to the exercise definition, I'm using EAGER so I always have its name ready
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private int sets;

    @Column(nullable = false)
    private int reps;

    // I'm keeping weight optional so I can log bodyweight moves without a weight
    private Double weightKg;

    // I'm keeping this no-args constructor for JPA, that's the only reason it's here
    protected WorkoutExercise() {
    }

    public WorkoutExercise(Exercise exercise, int sets, int reps, Double weightKg) {
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weightKg = weightKg;
    }

    public Long getId() {
        return id;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public Double getWeightKg() {
        return weightKg;
    }
}
