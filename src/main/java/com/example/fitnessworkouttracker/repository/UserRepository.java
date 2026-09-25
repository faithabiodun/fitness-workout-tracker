package com.example.fitnessworkouttracker.repository;

import java.util.Optional;

import com.example.fitnessworkouttracker.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser,Long> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<AppUser> findByEmailIgnoreCase(String email);
}
