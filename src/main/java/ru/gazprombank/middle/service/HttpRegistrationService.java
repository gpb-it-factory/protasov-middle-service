package ru.gazprombank.middle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.dto.UserRegistrationRequest;

import static ru.gazprombank.middle.util.ErrorMessages.REGISTRATION_ERROR;
import static ru.gazprombank.middle.util.ErrorMessages.SERVER_ERROR;

@Service
@ConditionalOnProperty(name = "backend.client-type", havingValue = "http")
public class HttpRegistrationService implements RegistrationService {
    private final WebClient webClient;

    @Value("${backend.register.url}")
    private String registrationUrl;

    @Autowired
    public HttpRegistrationService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        try {
            return sendRequest(request);
        } catch (Exception e) {
            return new UserRegistrationResponse(false, SERVER_ERROR);
        }
    }

    private UserRegistrationResponse sendRequest(UserRegistrationRequest request) {
        return webClient.post()
                .uri(registrationUrl)
                .bodyValue(request)
                .retrieve()
                .toEntity(String.class)
                .map(this::processResponse)
                .onErrorResume(WebClientResponseException.class, this::handleWebClientResponseException)
                .block();
    }

    private Mono<UserRegistrationResponse> handleWebClientResponseException(WebClientResponseException ex) {
        if (ex.getStatusCode() == HttpStatus.CONFLICT) {
            return Mono.just(new UserRegistrationResponse(false, ex.getResponseBodyAsString()));
        } else if (ex.getStatusCode().is5xxServerError()) {
            return Mono.just(new UserRegistrationResponse(false, SERVER_ERROR));
        } else {
            return Mono.just(new UserRegistrationResponse(false, REGISTRATION_ERROR));
        }
    }

    private UserRegistrationResponse processResponse(ResponseEntity<String> responseEntity) {
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return new UserRegistrationResponse(true, null);
        } else {
            return new UserRegistrationResponse(false, REGISTRATION_ERROR);
        }
    }
}
