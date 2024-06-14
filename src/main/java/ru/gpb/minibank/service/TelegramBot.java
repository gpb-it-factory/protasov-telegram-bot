package ru.gpb.minibank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gpb.minibank.config.BotProperties;

@Slf4j
@Service
public final class TelegramBot extends TelegramLongPollingBot {
    private final String botUsername;
    private MessageHandler messageHandler;

    public TelegramBot(BotProperties botProperties) {
        super(botProperties.token());
        this.botUsername = botProperties.name();
    }

    @Autowired
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                messageHandler.processUpdate(update);
            } catch (TelegramApiException e) {
                log.error("Ошибка при обработке сообщения", e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
