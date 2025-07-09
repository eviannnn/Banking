package com.evelina.bankingApp.controller;

import com.evelina.bankingApp.dto.TransferRequest;
import com.evelina.bankingApp.security.SimpleTokenManager;
import com.evelina.bankingApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SimpleTokenManager tokenManager;

    @PostMapping("/deposit")
    public void deposit(@RequestHeader("Authorization") String bearerToken,
                        @RequestBody TransferRequest request) {
        String token = extractToken(bearerToken);
        String username = tokenManager.validateAndGetUsername(token);
        transactionService.deposit(username, request.amount());
    }

    @PostMapping("/withdraw")
    public void withdraw(@RequestHeader("Authorization") String bearerToken,
                         @RequestBody TransferRequest request) {
        String token = extractToken(bearerToken);
        String username = tokenManager.validateAndGetUsername(token);
        transactionService.withdraw(username, request.amount());
    }

    @PostMapping("/transfer")
    public void transfer(@RequestHeader("Authorization") String bearerToken,
                         @RequestBody TransferRequest request) {
        String token = extractToken(bearerToken);
        String fromUsername = tokenManager.validateAndGetUsername(token);
        transactionService.transfer(fromUsername, request.toUsername(), request.amount());
    }

    private String extractToken(String bearerToken) {
        return bearerToken.replace("Bearer ", "").trim();
    }
}

