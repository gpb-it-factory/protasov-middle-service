package ru.gazprombank.middle.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.dto.UserRegistrationRequest;

import java.util.Optional;

@Component
public class HttpBackendClientImpl implements BackendClient {
    private final WebClient webClient;

    @Value("${backend.register.url}")
    private String registrationUrl;

    @Autowired
    public HttpBackendClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public UserRegistrationResponse createUser(UserRegistrationRequest request) {
        try {
            ResponseEntity<String> entityResponse = sendRequest(request);
            return processResponse(entityResponse);
        } catch (Exception e) {
            return new UserRegistrationResponse(false, "Internal server error");
        }
    }

    private ResponseEntity<String> sendRequest(UserRegistrationRequest request) {
        WebClient.ResponseSpec response = webClient.post()
                .uri(registrationUrl)
                .bodyValue(request)
                .retrieve();
        return response.toEntity(String.class).block();
    }

    private UserRegistrationResponse processResponse(ResponseEntity<String> response) {
        return Optional.ofNullable(response)
                .map(entity -> {
                    HttpStatusCode status = entity.getStatusCode();
                    if (status.is2xxSuccessful()) {
                        return new UserRegistrationResponse(true, null);
                    } else if (status == HttpStatus.CONFLICT) {
                        return new UserRegistrationResponse(false, entity.getBody());
                    } else if (status.is5xxServerError()) {
                        return new UserRegistrationResponse(false, "Ошибка сервера.");
                    } else {
                        return new UserRegistrationResponse(false, "Ошибка при обработке запроса.");
                    }
                })
                .orElse(new UserRegistrationResponse(false, "Нет ответа от сервера."));
    }
}