package com.evelina.bankingApp.dto;

import java.math.BigDecimal;

public record TransferRequest(String toUsername, BigDecimal amount) {}
