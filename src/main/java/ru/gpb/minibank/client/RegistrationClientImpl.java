package ru.gpb.minibank.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.gpb.minibank.exception.UserRegistrationException;
import ru.gpb.minibank.service.dto.UserRegistrationRequest;


@Component
public class RegistrationClientImpl implements RegistrationClient {
    private final WebClient webClient;
    @Value("${services.middle.register.url}")
    private String registrationUrl;

    @Autowired
    public RegistrationClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void registerUser(UserRegistrationRequest request) throws UserRegistrationException {
        WebClient.ResponseSpec response = webClient.post()
                .uri(registrationUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new UserRegistrationException(errorBody))));

        try {
            response.bodyToMono(Void.class).block();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof UserRegistrationException) {
                throw (UserRegistrationException) e.getCause();
            }
            throw new UserRegistrationException("Ошибка соединения.");
        }
    }
}
