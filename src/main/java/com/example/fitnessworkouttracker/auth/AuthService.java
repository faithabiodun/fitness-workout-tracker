// I'm in my auth package where my signup/login rules live
package com.example.fitnessworkouttracker.auth;

import com.example.fitnessworkouttracker.auth.dto.AuthResponse;
import com.example.fitnessworkouttracker.auth.dto.LoginRequest;
import com.example.fitnessworkouttracker.auth.dto.SignUpRequest;
import com.example.fitnessworkouttracker.exception.DuplicateResourceException;
import com.example.fitnessworkouttracker.user.AppUser;
import com.example.fitnessworkouttracker.repository.UserRepository;
// I learned @Value reads my settings from application.properties
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
// I learned PasswordEncoder hashes and checks my passwords safely
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
// I learned @Service marks me as my business-logic bean
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

// I learned @Service lets Spring inject me into my controller
@Service
public class AuthService {

    // I'm holding my DB access, my hasher, my JWT writer, and my token settings
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expirationSeconds;

    // I'm injecting all my helpers plus my issuer/expiry from my config
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.issuer:workout-tracker}") String issuer,
            @Value("${app.jwt.expiration:3600}") long expirationSeconds
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
    }

    // I'm handling my signup: check email, hash password, save, return token
    public AuthResponse signup(SignUpRequest request) {

        // I'm rejecting my signup if my email is already taken
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateResourceException(
                    "Email is already registered"
            );
        }

        // I learned encode() hashes my password so I never store it plain
        String passwordHash =
                passwordEncoder.encode(request.password());

        // I'm making my new user (I lowercase my email to keep it consistent)
        AppUser user = new AppUser(
                request.name(),
                request.email().toLowerCase(),
                passwordHash
        );

        // I'm saving my user to my DB
        AppUser savedUser =
                userRepository.save(user);

        // I'm creating my JWT for my newly saved user
        String token =
                createToken(savedUser);

        // I'm returning my token with "Bearer" type
        return new AuthResponse(
                token,
                "Bearer"
        );
    }

    // I'm handling my login: find user, check password, return token
    public AuthResponse login(LoginRequest request) {

        // I'm finding my user by email, or I throw since I hide which part failed
        // I learned Optional.orElseThrow lets me handle my "user not found" case
        AppUser user = userRepository
                .findByEmailIgnoreCase(request.email())
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid email or password"
                        )
                );

        // I learned matches() compares my raw password to my stored hash
        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw new BadCredentialsException(
                    "Invalid email or password"
            );
        }

        // I'm making my JWT for my logged-in user
        String token = createToken(user);

        // I'm returning my token with "Bearer" type
        return new AuthResponse(
                token,
                "Bearer"
        );
    }

    // I'm building my signed JWT for a user in one place
    private String createToken(AppUser user) {

        // I'm grabbing my current time for my issued-at/expiry
        Instant now = Instant.now();

        // I'm setting my issuer, times, and claims for my token
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(
                        now.plus(expirationSeconds, ChronoUnit.SECONDS)
                )
                // I learned subject is my user id, so I know who my token belongs to
                .subject(
                        String.valueOf(user.getId())
                )
                // I'm adding my email as an extra claim in my token
                .claim("email", user.getEmail())
                .build();

        // I learned I'm signing my token header with HS256
        JwsHeader header =
                JwsHeader
                        .with(MacAlgorithm.HS256)
                        .build();

        // I'm encoding my header + claims into my final token string
        return jwtEncoder
                .encode(
                        JwtEncoderParameters.from(
                                header,
                                claims
                        )
                )
                .getTokenValue();
    }
}