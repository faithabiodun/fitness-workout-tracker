// I'm in my root package where my app starts
package com.example.fitnessworkouttracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This one annotation sets up all my auto-config so my app boots without manual wiring
@SpringBootApplication
public class FitnessWorkoutTrackerApplication {

    // I'm starting my whole app from this main method
    public static void main(String[] args) {
        SpringApplication.run(FitnessWorkoutTrackerApplication.class, args);
    }

}
