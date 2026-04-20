package com.petshop.application.service;

import com.petshop.application.dto.AuthResponse;
import com.petshop.application.dto.LoginRequest;
import com.petshop.domain.exception.InvalidCredentialsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AuthService {
    
    @Inject
    TokenIssuerService tokenIssuerService;

    @ConfigProperty(name = "mock.user.admin.password", defaultValue = "admin123")
    String adminPassword;

    @ConfigProperty(name = "mock.user.client.password", defaultValue = "cliente123")
    String clientPassword;

    public AuthResponse authenticate(LoginRequest request) {
        // Usuários simulados em memória para fins didáticos (Sem DB)
        if ("admin".equals(request.getUsername()) && adminPassword.equals(request.getPassword())) {
            String token = tokenIssuerService.generateToken(request.getUsername(), "ADMIN");
            return new AuthResponse(token, 3600);
        } else if ("cliente".equals(request.getUsername()) && clientPassword.equals(request.getPassword())) {
            String token = tokenIssuerService.generateToken(request.getUsername(), "CLIENT");
            return new AuthResponse(token, 3600);
        }
        
        throw new InvalidCredentialsException("Usuário ou senha inválidos.");
    }
}
