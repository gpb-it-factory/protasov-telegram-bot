package ru.gpb.minibank.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class Start implements Command {
    private final String commandName;

    public Start() {
        commandName = "/start";
    }
    @Override
    public boolean canExecute(String message) {
        return commandName.equals(message);
    }

    @Override
    public String execute(Update update) {
        return "Привет. Я — бот, созданный для проекта МиниБанк " +
                "в рамках бэкенд-академии GPB IT FACTORY 2024. Вот что я умею делать:\n" +
                "/register - регистрация в нашем банке;\n" +
                "/createaccount - открытие счёта;\n" +
                "/currentbalance - проверка баланса;\n" +
                "/transfer [пользователь] [сумма] - перевод средств.";
    }
}
