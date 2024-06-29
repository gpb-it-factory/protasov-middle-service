package ru.gazprombank.middle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.gazprombank.middle.dto.*;

import java.util.List;

import static ru.gazprombank.middle.util.ErrorMessages.*;

@Component
@ConditionalOnProperty(name = "backend.client-type", havingValue = "http")
public class HttpAccountService implements AccountService {
    private final WebClient webClient;

    @Value("${backend.create-account.url}")
    private String createAccountUrl;
    @Value("${backend.current-balance.url}")
    private String getBalanceUrl;

    @Autowired
    public HttpAccountService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public CreateAccountResponse createAccount(Long id, CreateAccountRequest request) {
        try {
            return webClient.post()
                    .uri(createAccountUrl, id)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            this::handleErrorResponse)
                    .toBodilessEntity()
                    .map(response -> new CreateAccountResponse(true, null))
                    .onErrorResume(ResponseStatusException.class, e ->
                            Mono.just(new CreateAccountResponse(false, e.getReason())))
                    .block();
        } catch (Exception e) {
            return new CreateAccountResponse(false, SERVER_ERROR);
        }
    }

    @Override
    public CurrentBalanceResponse getCurrentBalance(Long id) {
        try {
            return webClient.get()
                    .uri(getBalanceUrl, id)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            this::handleErrorResponse)
                    .bodyToMono(new ParameterizedTypeReference<List<AccountDTO>>() {})
                    .map(accounts -> new CurrentBalanceResponse(true, accounts, null))
                    .onErrorResume(ResponseStatusException.class, e ->
                            Mono.just(new CurrentBalanceResponse(false, null, e.getReason())))
                    .block();
        } catch (Exception e) {
            return new CurrentBalanceResponse(false, null, SERVER_ERROR);
        }
    }

    private Mono<Throwable> handleErrorResponse(ClientResponse response) {
        return response.bodyToMono(ErrorResponse.class)
                .map(error -> new ResponseStatusException(response.statusCode(), error.message()));
    }
}