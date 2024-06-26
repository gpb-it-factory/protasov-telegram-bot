package ru.gpb.minibank.service.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(Long userId, @NotBlank String accountName) {
}
