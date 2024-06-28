package ru.gpb.minibank.service.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.gpb.minibank.client.AccountClient;
import ru.gpb.minibank.exception.AccountException;
import ru.gpb.minibank.service.dto.BalanceResponse;

import java.util.List;
import java.util.Locale;

@Component
public class CurrentBalance implements Command {
    private final String commandName;
    private final AccountClient accountClient;

    @Autowired
    public CurrentBalance(AccountClient accountClient) {
        this.commandName = "/currentbalance";
        this.accountClient = accountClient;
    }

    @Override
    public boolean canExecute(String message) {
        return commandName.equals(message);
    }

    @Override
    public String execute(Update update) {
        Long userId = update.getMessage().getChatId();
        try {
            List<BalanceResponse> balances = accountClient.getCurrentBalances(userId);
            return buildBalanceMessage(balances);
        } catch (AccountException e) {
            return e.getMessage();
        }
    }

    private String buildBalanceMessage(List<BalanceResponse> balances) {
        if (balances.isEmpty()) {
            return "У Вас нет открытых счетов.";
        }
        return formatBalanceMessage(balances);
    }

    private String formatBalanceMessage(List<BalanceResponse> balances) {
        StringBuilder response = new StringBuilder("Ваши текущие счета:\n");
        for (BalanceResponse balance : balances) {
            response.append(String.format(Locale.US, "Счет '%s': %.2f\n", balance.accountName(), balance.amount()));
        }
        return response.toString().trim();
    }
}
