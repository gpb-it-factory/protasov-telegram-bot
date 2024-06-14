package ru.gpb.minibank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gpb.minibank.client.RegistrationClient;
import ru.gpb.minibank.exception.UserRegistrationException;
import ru.gpb.minibank.service.dto.UserRegistrationRequest;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageHandlerTest {

    @Mock
    private MessageSender messageSenderImpl;
    @Mock
    private RegistrationClient registrationClient;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private User user;

    @InjectMocks
    private MessageHandler messageHandler;

    private UserRegistrationRequest request;

    private static final long CHAT_ID = 543L;
    private static final String USER_NAME = "testUser";
    private static final String SUCCESS_MESSAGE = "Вы успешно зарегестрированы!";
    private static final String CONNECTION_ERROR_MESSAGE = "Ошибка соединения.";
    private static final String REGISTRATION_ERROR_MESSAGE = "Ошибка регистрации. HTTP 400 Bad Request";

    @BeforeEach
    void setUp() {
        when(update.getMessage()).thenReturn(message);
        when(message.getFrom()).thenReturn(user);
        when(user.getUserName()).thenReturn(USER_NAME);
        when(message.getText()).thenReturn("/register");
        when(message.getChatId()).thenReturn(CHAT_ID);
        request = new UserRegistrationRequest(CHAT_ID, USER_NAME);
    }

    @Test
    void testRegisterSuccessfulRegistration() throws TelegramApiException, UserRegistrationException {
        doNothing().when(registrationClient).registerUser(request);

        messageHandler.processUpdate(update);

        verify(messageSenderImpl).sendMessage(eq(CHAT_ID), eq(SUCCESS_MESSAGE));
    }

    @Test
    void testRegisterWithHttpStatusException() throws UserRegistrationException, TelegramApiException {
        doThrow(new UserRegistrationException(REGISTRATION_ERROR_MESSAGE))
                .when(registrationClient).registerUser(request);

        messageHandler.processUpdate(update);

        verify(messageSenderImpl).sendMessage(eq(CHAT_ID), eq(REGISTRATION_ERROR_MESSAGE));
    }

    @Test
    void testRegisterWithRestException() throws UserRegistrationException, TelegramApiException {
        doThrow(new UserRegistrationException(CONNECTION_ERROR_MESSAGE))
                .when(registrationClient).registerUser(request);

        messageHandler.processUpdate(update);

        verify(messageSenderImpl).sendMessage(eq(CHAT_ID), eq(CONNECTION_ERROR_MESSAGE));
    }
}