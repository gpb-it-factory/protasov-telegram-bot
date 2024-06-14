package ru.gpb.minibank.client;

import ru.gpb.minibank.exception.UserRegistrationException;
import ru.gpb.minibank.service.dto.UserRegistrationRequest;

public interface RegistrationClient {
    void registerUser(UserRegistrationRequest request) throws UserRegistrationException;
}

