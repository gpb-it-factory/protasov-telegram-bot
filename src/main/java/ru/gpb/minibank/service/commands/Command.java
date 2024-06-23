package ru.gpb.minibank.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    boolean canExecute(String message);
    String execute(Update update);
}
