// I'm in my repository package where my DB queries live
package com.example.fitnessworkouttracker.repository;

// I need Optional because my find may return nothing
import java.util.Optional;

// I'm importing my user entity so I can query it
import com.example.fitnessworkouttracker.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

// I'm extending JpaRepository so I get save/find methods for free
public interface UserRepository extends JpaRepository<AppUser,Long> {
    // I'm checking if my email already exists (ignoring upper/lowercase)
    boolean existsByEmailIgnoreCase(String email);
    // I'm looking up my user by email, wrapped in Optional in case it's missing
    Optional<AppUser> findByEmailIgnoreCase(String email);
}
