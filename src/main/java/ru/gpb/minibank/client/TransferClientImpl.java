package ru.gpb.minibank.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.gpb.minibank.exception.TransferException;
import ru.gpb.minibank.service.dto.TransferRequest;
import ru.gpb.minibank.service.dto.TransferResponse;

@Component
public class TransferClientImpl implements TransferClient {
    private final WebClient webClient;

    @Value("${services.middle.transfer.url}")
    private String transferUrl;

    @Autowired
    public TransferClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public TransferResponse transferMoney(TransferRequest request) throws TransferException {
        WebClient.ResponseSpec response = webClient.post()
                .uri(transferUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new TransferException(errorBody))));

        try {
            return response.bodyToMono(TransferResponse.class).block();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof TransferException) {
                throw (TransferException) e.getCause();
            }
            throw new TransferException("Ошибка соединения при переводе средств.");
        }
    }
}