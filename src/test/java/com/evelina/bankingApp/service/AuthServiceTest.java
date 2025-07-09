package com.evelina.bankingApp.service;

import com.evelina.bankingApp.dto.LoginRequest;
import com.evelina.bankingApp.dto.RegistrationRequest;
import com.evelina.bankingApp.model.User;
import com.evelina.bankingApp.repository.UserRepository;
import com.evelina.bankingApp.security.SimpleTokenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SimpleTokenManager tokenManager;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        tokenManager = mock(SimpleTokenManager.class);

        authService = new AuthService(userRepository, passwordEncoder, tokenManager);
    }

    @Test
    void testRegister_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        authService.register(new RegistrationRequest("testuser", "password"));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegister_FailsWhenUserExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(new RegistrationRequest("testuser", "password"))
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(tokenManager.generateToken("testuser")).thenReturn("dummyToken");

        String token = String.valueOf(authService.login(new LoginRequest("testuser", "password")));

        assertThat(token).isEqualTo("dummyToken");
    }

    @Test
    void testLogin_FailsWhenUserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                authService.login(new LoginRequest("testuser", "password"))
        );
    }

    @Test
    void testLogin_FailsWhenPasswordInvalid() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                authService.login(new LoginRequest("testuser", "password"))
        );
    }
}
