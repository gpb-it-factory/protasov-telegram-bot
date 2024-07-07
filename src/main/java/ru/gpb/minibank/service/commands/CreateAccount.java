package ru.gpb.minibank.service.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.gpb.minibank.client.AccountClient;
import ru.gpb.minibank.exception.AccountException;
import ru.gpb.minibank.util.DTOFactory;
@Component
public class CreateAccount implements Command {
    private final String commandName;
    private final AccountClient accountClient;
    private final DTOFactory dtoFactory;

    private static final String ACCOUNT_NAME = "Акционный";

    @Autowired
    public CreateAccount(AccountClient accountClient) {
        this.commandName = "/createaccount";
        this.accountClient = accountClient;
        this.dtoFactory = new DTOFactory();
    }
    @Override
    public boolean canExecute(String message) {
        return commandName.equals(message);
    }

    @Override
    public String execute(Update update) {
        var request = dtoFactory.createAccountRequest(ACCOUNT_NAME);
        String answer;

        try {
            accountClient.createAccount(update.getMessage().getChatId(), request);
            answer = "Счет 'Акционный' открыт!";
        } catch (AccountException error) {
            answer = error.getMessage();
        }

        return answer;
    }
}

