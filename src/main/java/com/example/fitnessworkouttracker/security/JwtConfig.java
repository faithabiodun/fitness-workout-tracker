package com.example.fitnessworkouttracker.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfig {
    // I'm wiring my JWT signing setup here so my encoder and decoder share one secret
    @Bean
    public SecretKey secretKey(

            @Value("${app.jwt.secret}") String secret
    ) { return new SecretKeySpec( // I'm turning my config string into an HMAC-SHA256 key
            secret.getBytes(),
            "HmacSHA256"
    );
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey key) { // I'm signing my tokens here with the same secret
        return NimbusJwtEncoder
                .withSecretKey(key)
                .algorithm(MacAlgorithm.HS256) // I learned HS256 must match on both sides or my token fails
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder( // I'm verifying incoming tokens with that same secret
            SecretKey key,
            @Value("${app.jwt.issuer}") String issuer
    ) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        decoder.setJwtValidator( // I learned this checks my issuer so foreign tokens are rejected
                JwtValidators.createDefaultWithIssuer(issuer)
        );

        return decoder;
    }
}