package ru.gpb.minibank.service.dto;

public record TransferRequest(String from, String to, String amount) { }