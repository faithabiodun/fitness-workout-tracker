// I'm in my user package where my user model lives
package com.example.fitnessworkouttracker.user;

// I'm importing Instant so I can store my creation time
import java.time.Instant;
import jakarta.persistence.*;

// I learned @Entity means my class maps to a DB table
// I learned @Table names my table "users"
@Entity
@Table(name="users")
public class AppUser {
    // I learned @Id marks my primary key
    // I learned IDENTITY means my DB auto-increments my id
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    // I'm making my name required with max 100 chars
    @Column(nullable=false, length = 100)
    private String name;

    // I'm making my email required + unique so no duplicates
    @Column(nullable=false, length = 255, unique = true)
    private String email;

    // I'm storing only my password hash here, never the plain password
    @Column(nullable=false, length = 100)
    private String passwordHash;

    // I'm storing when my user was created
    @Column(nullable=false)
    private Instant createdAt;

    // I'm keeping this no-arg constructor for JPA (it needs it)
    protected  AppUser() {
    }
    // I'm using this constructor when I sign up a new user
    public AppUser(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = Instant.now();
    }

    // I'm reading my id (I use it as my JWT subject later)
    public Long getId(){
        return id;
    }
    // I'm reading my name
    public String getName() {
        return name;
    }

    // I'm updating my name
    public void setName(String name) {
        this.name = name;
    }

    // I'm reading my email
    public String getEmail() {
        return email;
    }

    // I'm updating my email
    public void setEmail(String email) {
        this.email = email;
    }

    // I'm reading my stored hash to check my login password
    public String getPasswordHash() {
        return passwordHash;
    }

    // I'm updating my stored hash (not the raw password)
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    // I'm reading when my account was created
    public Instant getCreatedAt() {
        return createdAt;
    }


}
