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
import ru.gpb.minibank.service.dto.CreateAccountRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class CreateAccountTests {
    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private CreateAccount createAccount;

    private Update update;
    private CreateAccountRequest request;

    private static final long CHAT_ID = 543L;
    private static final String USER_NAME = "testUser";
    private static final String SUCCESS_MESSAGE = "Счет 'Акционный' открыт!";
    private static final String CONNECTION_ERROR_MESSAGE = "Ошибка соединения.";
    public static final String ACCOUNT_ERROR = "Ошибка создания счета.";

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserName(USER_NAME);

        Chat chat = new Chat();
        chat.setId(CHAT_ID);

        Message message = new Message();
        message.setFrom(user);
        message.setText("/register");
        message.setChat(chat);

        update = new Update();
        update.setMessage(message);

        request = new CreateAccountRequest("Акционный");
    }

    @Test
    void whenCreateAccountIsSuccessful_thenReturnSuccessMessage() throws AccountException {
        doNothing().when(accountClient).createAccount(CHAT_ID, request);

        String result = createAccount.execute(update);

        assertEquals(SUCCESS_MESSAGE, result);
    }

    @Test
    void whenRestExceptionOccurs_thenReturnConnectionErrorMessage() throws AccountException {
        doThrow(new AccountException(CONNECTION_ERROR_MESSAGE))
                .when(accountClient).createAccount(CHAT_ID, request);

        String result = createAccount.execute(update);

        assertEquals(CONNECTION_ERROR_MESSAGE, result);
    }

    @Test
    void whenHttpStatusExceptionOccurs_thenReturnErrorMessage() throws AccountException {
        doThrow(new AccountException(ACCOUNT_ERROR))
                .when(accountClient).createAccount(CHAT_ID, request);

        String result = createAccount.execute(update);

        assertEquals(ACCOUNT_ERROR, result);
    }
}
