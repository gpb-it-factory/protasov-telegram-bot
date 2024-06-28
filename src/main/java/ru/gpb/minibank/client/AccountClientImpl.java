package ru.gpb.minibank.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.gpb.minibank.exception.AccountException;
import ru.gpb.minibank.service.dto.BalanceResponse;
import ru.gpb.minibank.service.dto.CreateAccountRequest;

import java.util.List;

@Component
public class AccountClientImpl implements AccountClient {
    private final WebClient webClient;
    @Value("${services.middle.create-account.url}")
    private String createAccountUrl;
    @Value("${services.middle.current-balance.url}")
    private String currentBalanceUrl;

    @Autowired
    public AccountClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }
    @Override
    public void createAccount(Long id, CreateAccountRequest request) throws AccountException {
        WebClient.ResponseSpec response = webClient.post()
                .uri(createAccountUrl, id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new AccountException(errorBody))));

        try {
            response.bodyToMono(Void.class).block();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof AccountException) {
                throw (AccountException) e.getCause();
            }
            throw new AccountException("Ошибка соединения при открытии счета.");
        }
    }
    @Override
    public List<BalanceResponse> getCurrentBalances(Long userId) throws AccountException {
        WebClient.ResponseSpec response = webClient.get()
                .uri(currentBalanceUrl, userId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new AccountException(errorBody))));

        try {
            return response.bodyToMono(new ParameterizedTypeReference<List<BalanceResponse>>() {}).block();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof AccountException) {
                throw (AccountException) e.getCause();
            }
            throw new AccountException("Ошибка соединения при получении балансов.");
        }
    }
}