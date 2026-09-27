package com.example.fitnessworkouttracker.workout;

import com.example.fitnessworkouttracker.workout.dto.WorkoutRequest;
import com.example.fitnessworkouttracker.workout.dto.WorkoutResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// I'm exposing my /api/workouts endpoints here, all of them need a logged-in user
@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    // JWT subject is the user id (see AuthService.createToken)
    // I'm reading my user id from the JWT via Authentication.getName() so I always scope to the caller
    private Long userId(Authentication auth) {
        return Long.parseLong(auth.getName());
    }

    // I'm creating a workout from what the frontend sends me
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutResponse create(@Valid @RequestBody WorkoutRequest request,
                                  Authentication auth) {
        return workoutService.createWorkout(userId(auth), request);
    }

    // I'm listing the caller's own workouts
    @GetMapping
    public List<WorkoutResponse> list(Authentication auth) {
        return workoutService.listWorkouts(userId(auth));
    }

    // I'm getting one of my own workouts by id
    @GetMapping("/{id}")
    public WorkoutResponse getOne(@PathVariable Long id, Authentication auth) {
        return workoutService.getWorkout(userId(auth), id);
    }

    // I'm patching just the status of one of my workouts
    @PatchMapping("/{id}/status")
    public WorkoutResponse updateStatus(@PathVariable Long id,
                                        @RequestParam WorkoutStatus status,
                                        Authentication auth) {
        return workoutService.updateStatus(userId(auth), id, status);
    }

    // I'm deleting one of my own workouts
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication auth) {
        workoutService.deleteWorkout(userId(auth), id);
    }
}
