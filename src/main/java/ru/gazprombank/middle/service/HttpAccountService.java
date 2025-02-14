package ru.gazprombank.middle.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.gazprombank.middle.config.MiddleProperties;
import ru.gazprombank.middle.dto.*;

import java.util.List;

import static ru.gazprombank.middle.util.ErrorMessages.*;

@Service
@ConditionalOnProperty(name = "backend.client-type", havingValue = "http")
public class HttpAccountService implements AccountService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private String createAccountUrl;
    private String getBalanceUrl;

    @Autowired
    public HttpAccountService(WebClient webClient, MiddleProperties middleProperties) {
        this.objectMapper = new ObjectMapper();
        this.webClient = webClient;
        this.createAccountUrl = middleProperties.getCreateAccount().getUrl();
        this.getBalanceUrl = middleProperties.getCurrentBalance().getUrl();
    }

    @Override
    public CreateAccountResponse createAccount(Long id, CreateAccountRequest request) {
        try {
            return webClient.post()
                    .uri(createAccountUrl, id)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                            .flatMap(body -> Mono.fromCallable(() -> {
                                ErrorResponse errorResponse = objectMapper.readValue(body, ErrorResponse.class);
                                    throw new ResponseStatusException(response.statusCode(),
                                            errorResponse.message());
                            })))
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
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                            .flatMap(body -> Mono.fromCallable(() -> {
                                ErrorResponse errorResponse = objectMapper.readValue(body, ErrorResponse.class);
                                throw new ResponseStatusException(response.statusCode(),
                                        errorResponse.message());
                            })))
                    .bodyToMono(new ParameterizedTypeReference<List<AccountDTO>>() {})
                    .map(accounts -> new CurrentBalanceResponse(true, accounts, null))
                    .onErrorResume(ResponseStatusException.class, e ->
                            Mono.just(new CurrentBalanceResponse(false, null, e.getReason())))
                    .block();
        } catch (Exception e) {
            return new CurrentBalanceResponse(false, null, SERVER_ERROR);
        }
    }
}