package ru.gpb.minibank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gpb.minibank.client.RegistrationClient;
import ru.gpb.minibank.exception.UserRegistrationException;
import ru.gpb.minibank.client.RegistrationClientImpl;
import ru.gpb.minibank.service.dto.UserRegistrationRequest;

@Slf4j
@Component
public final class MessageHandler {
    private final MessageSender messageSender;
    private final RegistrationClient registrationClient;

    @Autowired
    public MessageHandler(MessageSender messageSender, RegistrationClient registrationClient) {
        this.messageSender = messageSender;
        this.registrationClient = registrationClient;
    }

    public void processUpdate(Update update) throws TelegramApiException {
        Message message = update.getMessage();
        String messageText = message.getText();
        long chatId = message.getChatId();
        String userName = getUserName(message.getFrom());

        log.info("Получено сообщение: \"{}\" от пользователя: {}", messageText, chatId);

        switch (messageText) {
            case "/start" -> handleStartCommand(chatId);
            case "/ping" -> handlePingCommand(chatId);
            case "/register" -> handleRegisterCommand(chatId, userName);
            default -> handleUnknownCommand(chatId);
        }
    }

    public static String getUserName(User user) {
        if (user.getUserName() != null) {
            return user.getUserName();
        }
        return null;
    }

    public void handleStartCommand(long chatId) throws TelegramApiException {
        messageSender.sendMessage(chatId, "Привет. Я — бот, созданный для проекта МиниБанк " +
                "в рамках бэкенд-академии GPB IT FACTORY 2024. " +
                "Пока что я умею отвечать на команду /ping.");
        log.info("Выполнена команда /start для пользователя: {}", chatId);
    }

    public void handlePingCommand(long chatId) throws TelegramApiException {
        messageSender.sendMessage(chatId, "pong");
        log.info("Выполнена команда /ping для пользователя: {}", chatId);
    }

    public void handleRegisterCommand(long chatId, String userName) throws TelegramApiException {
        String answer;
        UserRegistrationRequest request = new UserRegistrationRequest(chatId, userName);

        try {
            registrationClient.registerUser(request);
            answer = "Вы успешно зарегестрированы!";
        } catch (UserRegistrationException error) {
            answer = error.getMessage();
        }

        log.info(answer, chatId, userName);
        log.info("Выполнена команда /register для пользователя: {}", chatId);
        messageSender.sendMessage(chatId, answer);
    }

    public void handleUnknownCommand(long chatId) throws TelegramApiException {
        messageSender.sendMessage(chatId, "Неизвестная команда.");
        log.info("Получена неизвестная команда от пользователя {}", chatId);
    }
}
