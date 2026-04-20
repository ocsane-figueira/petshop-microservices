package com.petshop.application.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.HashSet;

@ApplicationScoped
public class TokenIssuerService {

    public String generateToken(String username, String role) {
        long duration = 3600; // 1 hora de validade

        return Jwt.issuer("petshop-auth")
                  .upn(username)
                  .groups(new HashSet<>(Arrays.asList(role)))
                  .expiresIn(duration)
                  .sign();
    }
}
