package ru.gpb.minibank.service.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.gpb.minibank.service.dto.TransferRequest;
import ru.gpb.minibank.client.TransferClient;
import ru.gpb.minibank.exception.TransferException;
import ru.gpb.minibank.service.dto.TransferResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferTest {
    @Mock
    private TransferClient transferClient;

    @InjectMocks
    private Transfer transfer;

    private Update update;
    private TransferRequest request;

    private static final String FROM_USER_NAME = "testUser";
    private static final String TO_USER_NAME = "ner1";
    private static final String AMOUNT = "1000.12";
    private static final String TRANSFER_ID = "TRX123";
    private static final String SUCCESS_MESSAGE = String.format(
            "Перевод пользователю %s на сумму %s выполнен. ID перевода: %s",
            AMOUNT, TO_USER_NAME, TRANSFER_ID
    );
    private static final String INVALID_FORMAT_MESSAGE = "Неправильный формат команды. " +
            "Используйте: /transfer [пользователь] [сумма]\n" +
            "Никнейм должен содержать только английские буквы и быть не короче 4 символов.\n" +
            "Сумма должна быть больше 0.";
    private static final String TRANSFER_ERROR = "Ошибка при переводе средств.";

    @BeforeEach
    void setUp() {
        User user = new User();

        Chat chat = new Chat();
        chat.setUserName(FROM_USER_NAME);

        Message message = new Message();
        message.setFrom(user);
        message.setText("/transfer " + TO_USER_NAME + " " + AMOUNT);
        message.setChat(chat);

        update = new Update();
        update.setMessage(message);

        request = new TransferRequest(FROM_USER_NAME, TO_USER_NAME, AMOUNT);
    }

    @Test
    void transferSuccessful() throws TransferException {
        when(transferClient.transferMoney(request)).thenReturn(new TransferResponse(TRANSFER_ID));

        String result = transfer.execute(update);

        assertEquals(SUCCESS_MESSAGE, result);
    }

    @Test
    void returnErrorMessageWhenTransferExceptionOccurs() throws TransferException {
        when(transferClient.transferMoney(request)).thenThrow(new TransferException(TRANSFER_ERROR));

        String result = transfer.execute(update);

        assertEquals(TRANSFER_ERROR, result);
    }

    @Test
    @DisplayName("Проверяет сообщение об ошибке при вводе суммы с ведущим нулем")
    void transferWithLeadingZeroInAmount() {
        update.getMessage().setText("/transfer validUser 0100.50");

        String result = transfer.execute(update);

        assertEquals(INVALID_FORMAT_MESSAGE, result);
    }

    @Test
    @DisplayName("Проверяет сообщение об ошибке при вводе суммы с более чем двумя знаками после запятой")
    void transferWithMoreThanTwoDecimalPlaces() {
        update.getMessage().setText("/transfer validUser 100.501");

        String result = transfer.execute(update);

        assertEquals(INVALID_FORMAT_MESSAGE, result);
    }

    @Test
    @DisplayName("Проверяет сообщение об ошибке с никнеймом, который начинается с цифры")
    void transferWithUsernameStartingWithDigit() {
        update.getMessage().setText("/transfer 1awr 1000.5");

        String result = transfer.execute(update);

        assertEquals(INVALID_FORMAT_MESSAGE, result);
    }

    @Test
    @DisplayName("Проверяет сообщение об ошибке с никнеймом, который заканчивается симвлом подчеркивания")
    void transferWithUsernameEndingWithUnderscore() {
        update.getMessage().setText("/transfer sdk3_ 1000.5");

        String result = transfer.execute(update);

        assertEquals(INVALID_FORMAT_MESSAGE, result);
    }
}