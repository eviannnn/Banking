package com.evelina.bankingApp.service;

import com.evelina.bankingApp.dto.AuthResponse;
import com.evelina.bankingApp.dto.LoginRequest;
import com.evelina.bankingApp.dto.RegistrationRequest;
import com.evelina.bankingApp.model.User;
import com.evelina.bankingApp.repository.UserRepository;
import com.evelina.bankingApp.security.SimpleTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SimpleTokenManager tokenManager;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       SimpleTokenManager tokenManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenManager = tokenManager;
    }

    public void register(RegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("Attempt to register already existing username: {}", request.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(BigDecimal.ZERO);
        userRepository.save(user);

        log.info("Registered new user: {}", request.getUsername());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("Login failed — user not found: {}", request.getUsername());
                    return new IllegalArgumentException("Invalid username or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed — incorrect password for user: {}", request.getUsername());
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = tokenManager.generateToken(user.getUsername());
        log.info("User logged in: {}", request.getUsername());
        return new AuthResponse(token);
    }
}
