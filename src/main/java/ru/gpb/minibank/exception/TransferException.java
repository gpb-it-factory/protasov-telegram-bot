package ru.gpb.minibank.exception;

public class TransferException extends Exception {
    public TransferException(String message) {
        super(message);
    }
}