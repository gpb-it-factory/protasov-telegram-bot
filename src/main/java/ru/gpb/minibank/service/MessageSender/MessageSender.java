package ru.gpb.minibank.service.MessageSender;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface MessageSender {
    void sendMessage(long chatId, String text) throws TelegramApiException;
}

