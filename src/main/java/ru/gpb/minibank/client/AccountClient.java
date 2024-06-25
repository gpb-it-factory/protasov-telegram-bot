package ru.gpb.minibank.client;

import ru.gpb.minibank.exception.AccountCreationException;
import ru.gpb.minibank.service.dto.CreateAccountRequest;

public interface AccountClient {
    void createAccount(CreateAccountRequest request) throws AccountCreationException;
}
