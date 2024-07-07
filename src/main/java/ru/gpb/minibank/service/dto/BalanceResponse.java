package ru.gpb.minibank.service.dto;

import java.math.BigDecimal;

public record BalanceResponse(String accountName, BigDecimal amount) { }