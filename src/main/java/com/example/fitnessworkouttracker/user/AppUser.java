package com.example.fitnessworkouttracker.user;

import java.time.Instant;
import jakarta.persistence.*;

@Entity
@Table(name="users")
public class AppUser {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length = 100)
    private String name;

    @Column(nullable=false, length = 255, unique = true)
    private String email;

    @Column(nullable=false, length = 100)
    private String passwordHash;

    @Column(nullable=false)
    private Instant createdAt;

    protected  AppUser() {
    }
    public AppUser(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = Instant.now();
    }

    public Long getId(){
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }


}
