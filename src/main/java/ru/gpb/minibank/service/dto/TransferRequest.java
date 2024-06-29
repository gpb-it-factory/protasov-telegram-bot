package ru.gpb.minibank.service.dto;

public record TransferRequest(Long fromUserId, String toUsername, String amount) { }