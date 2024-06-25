package ru.gpb.minibank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.gpb.minibank.service.commands.Command;
import ru.gpb.minibank.service.commands.Unknown;
import java.util.List;

@Slf4j
@Component
public class MessageHandler {
    private final List<Command> commands;
    private final Command unknownCommand;

    @Autowired
    public MessageHandler(List<Command> commands) {
        this.commands = commands;
        this.unknownCommand = new Unknown();
    }

    public String getResponse(Update update) {
        Message message = update.getMessage();
        String messageText = message.getText();

        log.info("Получено сообщение: \"{}\" от пользователя: {}", messageText, message.getChatId());
        return findAndExecuteCommand(messageText, update);
    }

    private String findAndExecuteCommand(String messageText,  Update update) {
        return commands.stream()
                .filter(cmd -> cmd.canExecute(messageText))
                .findFirst()
                .orElse(unknownCommand)
                .execute(update);
    }
}
