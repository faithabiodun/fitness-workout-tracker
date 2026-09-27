package com.example.fitnessworkouttracker.workout;

import com.example.fitnessworkouttracker.user.AppUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workouts")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(nullable = false)
    private LocalDate performedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkoutStatus status;

    @Column(length = 500)
    private String notes;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> exercises = new ArrayList<>();

    @Column(nullable = false)
    private Instant createdAt;

    protected Workout() {
    }

    public Workout(AppUser user, LocalDate performedDate, WorkoutStatus status, String notes) {
        this.user = user;
        this.performedDate = performedDate;
        this.status = status;
        this.notes = notes;
        this.createdAt = Instant.now();
    }

    public void addExercise(WorkoutExercise item) {
        item.setWorkout(this);
        this.exercises.add(item);
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public LocalDate getPerformedDate() {
        return performedDate;
    }

    public void setPerformedDate(LocalDate performedDate) {
        this.performedDate = performedDate;
    }

    public WorkoutStatus getStatus() {
        return status;
    }

    public void setStatus(WorkoutStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<WorkoutExercise> getExercises() {
        return exercises;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
