package ru.gpb.minibank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Slf4j
@Component
public class MessageReceiver {

    private final MessageSender messageSender;

     @Autowired
    public MessageReceiver(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void handleMessage(Update update) throws TelegramApiException {
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        log.info("Получено сообщение: {} от пользователя: {}", messageText, chatId);

        switch (messageText) {
            case "/start" -> handleStartCommand(chatId);
            case "/ping" -> handlePingCommand(chatId);
            default -> handleUnknownCommand(chatId);
        }
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

    public void handleUnknownCommand(long chatId) throws TelegramApiException {
        messageSender.sendMessage(chatId, "Неизвестная команда.");
        log.info("Получена неизвестная команда от пользователя {}", chatId);
    }
}
