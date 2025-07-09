package com.evelina.bankingApp.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimpleTokenManager {
    private final Map<String, String> activeTokens = new ConcurrentHashMap<>();

    public String generateToken(String username) {
        String token = UUID.randomUUID().toString();
        activeTokens.put(token, username);
        return token;
    }

    public String validateAndGetUsername(String token) {
        String username = activeTokens.get(token);
        if (username == null) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
        return username;
    }

    public void logout(String token) {
        activeTokens.remove(token);
    }

    public Optional<String> getUsernameByToken(String token) {
        return Optional.ofNullable(activeTokens.get(token));
    }

    public void removeToken(String token) {
        activeTokens.remove(token);
    }
}
