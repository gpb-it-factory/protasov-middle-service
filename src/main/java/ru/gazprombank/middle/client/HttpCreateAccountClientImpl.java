package ru.gazprombank.middle.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gazprombank.middle.dto.CreateAccountRequest;
import ru.gazprombank.middle.dto.CreateAccountResponse;

import java.util.Optional;

import static ru.gazprombank.middle.util.ErrorMessages.*;

@Component
public class HttpCreateAccountClientImpl implements CreateAccountClient {
    private final WebClient webClient;

    @Value("${backend.create-account.url}")
    private String createAccountUrl;

    @Autowired
    public HttpCreateAccountClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public CreateAccountResponse createAccount(Long id, CreateAccountRequest request) {
        try {
            ResponseEntity<String> entityResponse = sendRequest(id, request);
            return processResponse(entityResponse);
        } catch (Exception e) {
            return new CreateAccountResponse(false, SERVER_ERROR);
        }
    }

    private ResponseEntity<String> sendRequest(Long id, CreateAccountRequest request) {
        WebClient.ResponseSpec response = webClient.post()
                .uri(createAccountUrl, id)
                .bodyValue(request)
                .retrieve();
        return response.toEntity(String.class).block();
    }

    private CreateAccountResponse processResponse(ResponseEntity<String> response) {
        return Optional.ofNullable(response)
                .map(entity -> {
                    HttpStatusCode status = entity.getStatusCode();
                    if (status.is2xxSuccessful()) {
                        return new CreateAccountResponse(true, null);
                    } else if (status == HttpStatus.CONFLICT) {
                        return new CreateAccountResponse(false, entity.getBody());
                    } else if (status == HttpStatus.NOT_FOUND) {
                        return new CreateAccountResponse(false, entity.getBody());
                    }
                    else if (status.is5xxServerError()) {
                        return new CreateAccountResponse(false, SERVER_ERROR);
                    } else {
                        return new CreateAccountResponse(false, ACCOUNT_ERROR);
                    }
                })
                .orElse(new CreateAccountResponse(false, SERVER_ERROR));
    }
}
