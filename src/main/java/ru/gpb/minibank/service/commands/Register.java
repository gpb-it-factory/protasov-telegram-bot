package ru.gpb.minibank.service.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.gpb.minibank.client.RegistrationClient;
import ru.gpb.minibank.exception.UserRegistrationException;
import ru.gpb.minibank.util.DTOFactory;

@Component
public class Register implements Command {
    private final String commandName;
    private final RegistrationClient registrationClient;
    private final DTOFactory dtoFactory;

    @Autowired
    public Register(RegistrationClient registrationClient) {
        commandName = "/register";
        this.registrationClient = registrationClient;
        this.dtoFactory = new DTOFactory();
    }

    @Override
    public boolean canExecute(String message) {
        return commandName.equals(message);
    }

    @Override
    public String execute(Update update) {
        var request = dtoFactory.createUserRegistrationRequest(update);
        String answer;

        try {
            registrationClient.registerUser(request);
            answer = "Вы успешно зарегестрированы!";
        } catch (UserRegistrationException error) {
            answer = error.getMessage();
        }

        return answer;
    }
}
