package ru.gpb.minibank.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.gpb.minibank.exception.AccountCreationException;
import ru.gpb.minibank.service.dto.CreateAccountRequest;

@Component
public class AccountClientImpl implements AccountClient {
    private final WebClient webClient;
    @Value("${services.middle.create-account.url}")
    private String createAccountUrl;

    @Autowired
    public AccountClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }
    @Override
    public void createAccount(CreateAccountRequest request) throws AccountCreationException {
        WebClient.ResponseSpec response = webClient.post()
                .uri(createAccountUrl, request.userId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new AccountCreationException(errorBody))));

        try {
            response.bodyToMono(Void.class).block();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof AccountCreationException) {
                throw (AccountCreationException) e.getCause();
            }
            throw new AccountCreationException("Ошибка соединения.");
        }
    }
}
