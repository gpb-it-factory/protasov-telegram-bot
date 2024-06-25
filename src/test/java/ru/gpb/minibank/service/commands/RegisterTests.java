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
import ru.gpb.minibank.client.RegistrationClient;
import ru.gpb.minibank.exception.UserRegistrationException;
import ru.gpb.minibank.service.dto.UserRegistrationRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class RegisterTests {
    @Mock
    private RegistrationClient registrationClient;

    @InjectMocks
    private Register register;

    private Update update;
    private UserRegistrationRequest request;

    private static final long CHAT_ID = 543L;
    private static final String USER_NAME = "testUser";
    private static final String SUCCESS_MESSAGE = "Вы успешно зарегестрированы!";
    private static final String CONNECTION_ERROR_MESSAGE = "Ошибка соединения.";
    private static final String REGISTRATION_ERROR_MESSAGE = "Ошибка регистрации.";

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

        request = new UserRegistrationRequest(CHAT_ID, USER_NAME);
    }

    @Test
    void whenRegistrationIsSuccessful_thenReturnSuccessMessage() throws UserRegistrationException {
        doNothing().when(registrationClient).registerUser(request);

        String result = register.execute(update);

        assertEquals(SUCCESS_MESSAGE, result);
    }

    @Test
    void whenHttpStatusExceptionOccurs_thenReturnRegistrationErrorMessage() throws UserRegistrationException {
        doThrow(new UserRegistrationException(REGISTRATION_ERROR_MESSAGE))
                .when(registrationClient).registerUser(request);

        String result = register.execute(update);

        assertEquals(REGISTRATION_ERROR_MESSAGE, result);
    }

    @Test
    void whenRestExceptionOccurs_thenReturnConnectionErrorMessage() throws UserRegistrationException {
        doThrow(new UserRegistrationException(CONNECTION_ERROR_MESSAGE))
                .when(registrationClient).registerUser(request);

        String result = register.execute(update);

        assertEquals(CONNECTION_ERROR_MESSAGE, result);
    }
}