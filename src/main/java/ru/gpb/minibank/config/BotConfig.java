package ru.gpb.minibank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.gpb.minibank.service.TelegramBot;

@Configuration
public class BotConfig {

    private final BotProperties botProperties;

    @Autowired
    public BotConfig(BotProperties botProperties) {
        this.botProperties = botProperties;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    public TelegramBot yourTelegramBot(TelegramBotsApi telegramBotsApi) throws TelegramApiException {
        var bot = new TelegramBot(botProperties);
        telegramBotsApi.registerBot(bot);
        return bot;
    }


}