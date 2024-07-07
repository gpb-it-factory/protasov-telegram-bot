package ru.gpb.minibank.service.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.gpb.minibank.client.AccountClient;
import ru.gpb.minibank.exception.AccountException;
import ru.gpb.minibank.service.dto.BalanceResponse;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrentBalanceTest {
    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private CurrentBalance currentBalance;

    private Update update;

    private static final long CHAT_ID = 543L;
    private static final String USER_NAME = "testUser";
    public static final String NO_OPEN_ACCOUNTS = "У Вас нет открытых счетов.";
    public static final String GETTING_BALANCES_ERROR = "Ошибка получения балансов.";


    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserName(USER_NAME);

        Chat chat = new Chat();
        chat.setId(CHAT_ID);

        Message message = new Message();
        message.setFrom(user);
        message.setText("/currentbalance");
        message.setChat(chat);

        update = new Update();
        update.setMessage(message);
    }
    @Test
    void currentBalanceWithoutAccounts() throws AccountException {
        when(accountClient.getCurrentBalances(CHAT_ID)).thenReturn(Collections.emptyList());

        String result = currentBalance.execute(update);

        assertEquals(NO_OPEN_ACCOUNTS, result);
    }

    @Test
    void currentBalanceWithAccount() throws AccountException {
        List<BalanceResponse> balances = Collections.singletonList(
                new BalanceResponse("Акционный", new BigDecimal("5000.00"))
        );
        when(accountClient.getCurrentBalances(CHAT_ID)).thenReturn(balances);

        String result = currentBalance.execute(update);

        assertEquals("Ваши текущие счета:\nСчет 'Акционный': 5000.00", result);
    }

    @Test
    void returnErrorMessageWhenAccountExceptionOccurs() throws AccountException {
        when(accountClient.getCurrentBalances(CHAT_ID)).thenThrow(new AccountException(GETTING_BALANCES_ERROR));

        String result = currentBalance.execute(update);

        assertEquals(GETTING_BALANCES_ERROR, result);
    }
}