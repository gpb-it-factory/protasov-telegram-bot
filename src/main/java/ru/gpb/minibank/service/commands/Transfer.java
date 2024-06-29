package ru.gpb.minibank.service.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.gpb.minibank.client.TransferClient;
import ru.gpb.minibank.exception.TransferException;
import ru.gpb.minibank.service.dto.TransferResponse;
import ru.gpb.minibank.util.DTOFactory;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Transfer implements Command {
    private static final String COMMAND_PATTERN =
            "^/transfer (@?[a-zA-Z][a-zA-Z0-9]*(?:_[a-zA-Z0-9]+)*) ([1-9]\\d*(?:\\.\\d{1,2})?)$";

    private static final Pattern pattern = Pattern.compile(COMMAND_PATTERN);

    private final TransferClient transferClient;
    private final DTOFactory dtoFactory;

    @Autowired
    public Transfer(TransferClient transferClient) {
        this.transferClient = transferClient;
        this.dtoFactory = new DTOFactory();
    }

    @Override
    public boolean canExecute(String message) {
        return message.startsWith("/transfer");
    }

    @Override
    public String execute(Update update) {
        Message message = update.getMessage();
        return processTransfer(message.getText(), message.getChatId())
                .orElse("""
                        Неправильный формат команды. Используйте: /transfer [пользователь] [сумма]
                        Никнейм должен содержать только английские буквы и быть не короче 4 символов.
                        Сумма должна быть больше 0.""");

    }

    private Optional<String> processTransfer(String message, Long fromUserId) {
        return extractTransferDetails(message)
                .map(details -> performTransfer(details, fromUserId));
    }

    private Optional<TransferDetails> extractTransferDetails(String message) {
        Matcher matcher = pattern.matcher(message);
        if (matcher.matches()) {
            String toUsername = matcher.group(1);
            String amount = matcher.group(2);
            return Optional.of(new TransferDetails(toUsername, amount));
        }
        return Optional.empty();
    }

    private String performTransfer(TransferDetails details, Long fromUserId) {
        var request = dtoFactory.createTransferRequest(fromUserId, details.toUsername, details.amount);
        try {
            TransferResponse response = transferClient.transferMoney(request);
            return String.format("Перевод пользователю %s на сумму %s выполнен. ID перевода: %s",
                    details.amount, details.toUsername, response.transferId());
        } catch (TransferException error) {
            return error.getMessage();
        }
    }

    private record TransferDetails(String toUsername, String amount) {}
}