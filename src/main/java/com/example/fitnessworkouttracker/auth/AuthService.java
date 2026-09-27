package com.example.fitnessworkouttracker.auth;

import com.example.fitnessworkouttracker.auth.dto.AuthResponse;
import com.example.fitnessworkouttracker.auth.dto.LoginRequest;
import com.example.fitnessworkouttracker.auth.dto.SignUpRequest;
import com.example.fitnessworkouttracker.exception.DuplicateResourceException;
import com.example.fitnessworkouttracker.user.AppUser;
import com.example.fitnessworkouttracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expirationSeconds;

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

    public AuthResponse signup(SignUpRequest request) {

        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateResourceException(
                    "Email is already registered"
            );
        }

        String passwordHash =
                passwordEncoder.encode(request.password());

        AppUser user = new AppUser(
                request.name(),
                request.email().toLowerCase(),
                passwordHash
        );

        AppUser savedUser =
                userRepository.save(user);

        String token =
                createToken(savedUser);

        return new AuthResponse(
                token,
                "Bearer"
        );
    }

    public AuthResponse login(LoginRequest request) {

        AppUser user = userRepository
                .findByEmailIgnoreCase(request.email())
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw new BadCredentialsException(
                    "Invalid email or password"
            );
        }

        String token = createToken(user);

        return new AuthResponse(
                token,
                "Bearer"
        );
    }

    private String createToken(AppUser user) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(
                        now.plus(expirationSeconds, ChronoUnit.SECONDS)
                )
                .subject(
                        String.valueOf(user.getId())
                )
                .claim("email", user.getEmail())
                .build();

        JwsHeader header =
                JwsHeader
                        .with(MacAlgorithm.HS256)
                        .build();

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