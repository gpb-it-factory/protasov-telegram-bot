package ru.gpb.minibank.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MessageSender extends DefaultAbsSender {

    public MessageSender(@Value("${bot.token}") String botToken) {
        super(new DefaultBotOptions(), botToken);
    }

    public void sendMessage(long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        execute(message);
    }
}
