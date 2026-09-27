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

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    // JWT subject is the user id (see AuthService.createToken)
    private Long userId(Authentication auth) {
        return Long.parseLong(auth.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutResponse create(@Valid @RequestBody WorkoutRequest request,
                                  Authentication auth) {
        return workoutService.createWorkout(userId(auth), request);
    }

    @GetMapping
    public List<WorkoutResponse> list(Authentication auth) {
        return workoutService.listWorkouts(userId(auth));
    }

    @GetMapping("/{id}")
    public WorkoutResponse getOne(@PathVariable Long id, Authentication auth) {
        return workoutService.getWorkout(userId(auth), id);
    }

    @PatchMapping("/{id}/status")
    public WorkoutResponse updateStatus(@PathVariable Long id,
                                        @RequestParam WorkoutStatus status,
                                        Authentication auth) {
        return workoutService.updateStatus(userId(auth), id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication auth) {
        workoutService.deleteWorkout(userId(auth), id);
    }
}
