package ru.gpb.minibank.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class Ping implements Command {
    private final String commandName;

    public Ping() {
        commandName = "/ping";
    }

    @Override
    public boolean canExecute(String message) {
        return commandName.equals(message);
    }

    @Override
    public String execute(Update update) {
        return "pong";
    }
}
