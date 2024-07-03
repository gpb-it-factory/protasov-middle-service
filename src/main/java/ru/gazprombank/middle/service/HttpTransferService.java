package ru.gazprombank.middle.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import reactor.core.publisher.Mono;
import ru.gazprombank.middle.config.MiddleProperties;
import ru.gazprombank.middle.dto.ErrorResponse;
import ru.gazprombank.middle.dto.TransferRequest;
import ru.gazprombank.middle.dto.TransferResponse;
import ru.gazprombank.middle.dto.TransferSuccessResponse;
import ru.gazprombank.middle.util.ErrorMessages;

@Service
@ConditionalOnProperty(name = "backend.client-type", havingValue = "http")
public class HttpTransferService implements TransferService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private String transferUrl;

    @Autowired
    public HttpTransferService(WebClient webClient, MiddleProperties middleProperties) {
        this.webClient = webClient;
        this.objectMapper = new ObjectMapper();
        this.transferUrl = middleProperties.getTransfer().getUrl();
    }

    @Override
    public TransferResponse transfer(TransferRequest request) {
        try {
            return webClient.post()
                    .uri(transferUrl)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                            .flatMap(body -> Mono.fromCallable(() -> {
                                ErrorResponse errorResponse = objectMapper.readValue(body, ErrorResponse.class);
                                throw new ResponseStatusException(response.statusCode(),
                                        errorResponse.message());
                            })))
                    .bodyToMono(TransferSuccessResponse.class)
                    .map(response -> new TransferResponse(true, response.transferId(), null))
                    .onErrorResume(ResponseStatusException.class, e ->
                            Mono.just(new TransferResponse(false, null, e.getReason())))
                    .block();
        } catch (Exception e) {
            return new TransferResponse(false, null, ErrorMessages.SERVER_ERROR);
        }
    }
}
