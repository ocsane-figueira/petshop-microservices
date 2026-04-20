package com.petshop.application.service;

import com.petshop.application.dto.AuthResponse;
import com.petshop.application.dto.LoginRequest;
import com.petshop.domain.exception.InvalidCredentialsException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AuthServiceTest {

    @Inject
    AuthService authService;

    @InjectMock
    TokenIssuerService tokenIssuerService;

    @Test
    public void testAuthenticate_AdminSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        when(tokenIssuerService.generateToken("admin", "ADMIN")).thenReturn("mock-token");

        AuthResponse response = authService.authenticate(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("mock-token", response.getToken());
    }

    @Test
    public void testAuthenticate_ClientSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("cliente");
        request.setPassword("cliente123");

        when(tokenIssuerService.generateToken("cliente", "CLIENT")).thenReturn("mock-token-client");

        AuthResponse response = authService.authenticate(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("mock-token-client", response.getToken());
    }

    @Test
    public void testAuthenticate_InvalidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setUsername("wrong");
        request.setPassword("wrong");

        Assertions.assertThrows(InvalidCredentialsException.class, () -> {
            authService.authenticate(request);
        });
    }
}
