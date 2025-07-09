package com.evelina.bankingApp.service;

import com.evelina.bankingApp.dto.UserDto;
import com.evelina.bankingApp.model.User;
import com.evelina.bankingApp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setBalance(BigDecimal.valueOf(100.0)); // или другое начальное значение
        userRepository.save(user);
    }

    public boolean authenticate(UserDto userDto) {
        Optional<User> userOpt = userRepository.findByUsername(userDto.getUsername());
        if (userOpt.isPresent()) {
            return passwordEncoder.matches(userDto.getPassword(), userOpt.get().getPassword());
        }
        return false;
    }
}

