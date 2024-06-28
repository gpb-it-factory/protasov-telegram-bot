package ru.gpb.minibank.client;

import ru.gpb.minibank.exception.AccountException;
import ru.gpb.minibank.service.dto.BalanceResponse;
import ru.gpb.minibank.service.dto.CreateAccountRequest;

import java.util.List;

public interface AccountClient {
    void createAccount(Long id, CreateAccountRequest request) throws AccountException;
    List<BalanceResponse> getCurrentBalances(Long userId) throws AccountException;
}