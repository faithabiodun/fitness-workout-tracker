package com.example.fitnessworkouttracker.report;

import com.example.fitnessworkouttracker.exercise.Exercise;
import com.example.fitnessworkouttracker.user.AppUser;
import com.example.fitnessworkouttracker.workout.Workout;
import com.example.fitnessworkouttracker.workout.WorkoutExercise;
import com.example.fitnessworkouttracker.workout.WorkoutRepository;
import com.example.fitnessworkouttracker.workout.WorkoutStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    WorkoutRepository workoutRepository;

    @InjectMocks
    ReportService reportService;

    private Workout workoutWith(AppUser user, WorkoutStatus status, int sets, int reps, Double weight) {
        Workout w = new Workout(user, LocalDate.now(), status, null);
        Exercise e = new Exercise("Squat", "Legs", "test");
        w.addExercise(new WorkoutExercise(e, sets, reps, weight));
        return w;
    }

    @Test
    void aggregatesCompletedWorkoutsOnly() {
        AppUser user = new AppUser("Ann", "ann@test.com", "hash");
        Workout done1 = workoutWith(user, WorkoutStatus.COMPLETED, 3, 10, 20.0);
        Workout done2 = workoutWith(user, WorkoutStatus.COMPLETED, 2, 5, 10.0);
        Workout planned = workoutWith(user, WorkoutStatus.PLANNED, 10, 10, 100.0);

        when(workoutRepository.findByUserIdAndStatus(1L, WorkoutStatus.COMPLETED))
                .thenReturn(List.of(done1, done2));
        when(workoutRepository.findByUserIdOrderByPerformedDateDesc(1L))
                .thenReturn(List.of(done1, done2, planned));

        ProgressReportResponse report = reportService.getProgressReport(1L);

        assertThat(report.totalWorkouts()).isEqualTo(3);
        assertThat(report.completedWorkouts()).isEqualTo(2);
        assertThat(report.totalSets()).isEqualTo(5);
        // (3*10) + (2*5) = 40 reps
        assertThat(report.totalReps()).isEqualTo(40);
        // (3*10*20) + (2*5*10) = 600 + 100 = 700
        assertThat(report.totalVolumeKg()).isEqualTo(700.0);
    }

    @Test
    void emptyWhenNoWorkouts() {
        when(workoutRepository.findByUserIdAndStatus(9L, WorkoutStatus.COMPLETED))
                .thenReturn(List.of());
        when(workoutRepository.findByUserIdOrderByPerformedDateDesc(9L))
                .thenReturn(List.of());

        ProgressReportResponse report = reportService.getProgressReport(9L);

        assertThat(report.totalWorkouts()).isZero();
        assertThat(report.completedWorkouts()).isZero();
        assertThat(report.totalVolumeKg()).isZero();
    }
}
