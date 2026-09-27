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

// I'm mapping this class to my workouts table in the database
@Entity
@Table(name = "workouts")
public class Workout {

    // I'm using this as my auto-generated primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // I'm linking each workout to one user (many workouts can belong to one user)
    // I'm using LAZY so I don't load the user unless I actually need it
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(nullable = false)
    private LocalDate performedDate;

    // I'm storing my status as a string (PLANNED/COMPLETED/SKIPPED) so it's readable in the database
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkoutStatus status;

    @Column(length = 500)
    private String notes;

    // I'm holding all my exercise lines here, cascade ALL saves them with me and orphanRemoval deletes lines I remove
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> exercises = new ArrayList<>();

    @Column(nullable = false)
    private Instant createdAt;

    // I'm keeping this no-args constructor protected because JPA needs it to load me
    protected Workout() {
    }

    // I'm stamping my creation time here so I know when I logged this workout
    public Workout(AppUser user, LocalDate performedDate, WorkoutStatus status, String notes) {
        this.user = user;
        this.performedDate = performedDate;
        this.status = status;
        this.notes = notes;
        this.createdAt = Instant.now();
    }

    // I'm using this helper so both sides of my relationship stay in sync when I add a line
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
