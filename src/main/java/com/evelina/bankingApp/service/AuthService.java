package com.evelina.bankingApp.service;

import com.evelina.bankingApp.dto.AuthResponse;
import com.evelina.bankingApp.dto.LoginRequest;
import com.evelina.bankingApp.dto.RegistrationRequest;
import com.evelina.bankingApp.model.User;
import com.evelina.bankingApp.repository.UserRepository;
import com.evelina.bankingApp.security.SimpleTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuthService {

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
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setBalance(BigDecimal.ZERO);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = tokenManager.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}
