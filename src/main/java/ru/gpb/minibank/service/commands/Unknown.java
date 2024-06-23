package ru.gpb.minibank.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

public class Unknown implements Command {
    @Override
    public boolean canExecute(String message) {
        return false;
    }

    @Override
    public String execute(Update update) {
        return "Неизвестная команда.";
    }
}
