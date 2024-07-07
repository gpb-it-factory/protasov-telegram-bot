package ru.gpb.minibank.client;

import ru.gpb.minibank.exception.TransferException;
import ru.gpb.minibank.service.dto.TransferRequest;
import ru.gpb.minibank.service.dto.TransferResponse;

public interface TransferClient {
    TransferResponse transferMoney(TransferRequest request) throws TransferException;
}