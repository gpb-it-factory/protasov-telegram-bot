package ru.gpb.minibank.util;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.gpb.minibank.service.dto.CreateAccountRequest;
import ru.gpb.minibank.service.dto.TransferRequest;
import ru.gpb.minibank.service.dto.UserRegistrationRequest;

public class DTOFactory {
    public UserRegistrationRequest createUserRegistrationRequest(Update update) {
        Message message = update.getMessage();
        return new UserRegistrationRequest(message.getChatId(), message.getFrom().getUserName());
    }

    public CreateAccountRequest createAccountRequest(String accountName) {
        return new CreateAccountRequest(accountName);
    }

    public TransferRequest createTransferRequest(String from, String to, String amount) {
        return new TransferRequest(from, to, amount);
    }
}