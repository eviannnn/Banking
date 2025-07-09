package com.evelina.bankingApp.service;

import com.evelina.bankingApp.model.User;
import com.evelina.bankingApp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    public void deposit(String username, BigDecimal amount) {
        User user = getUser(username);
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }

    public void withdraw(String username, BigDecimal amount) {
        User user = getUser(username);
        if (user.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);
    }

    @Transactional
    public void transfer(String fromUsername, String toUsername, BigDecimal amount) {
        User fromUser = getUser(fromUsername);
        User toUser = getUser(toUsername);

        if (fromUser.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        fromUser.setBalance(fromUser.getBalance().subtract(amount));
        toUser.setBalance(toUser.getBalance().add(amount));

        userRepository.save(fromUser);
        userRepository.save(toUser);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }
}

