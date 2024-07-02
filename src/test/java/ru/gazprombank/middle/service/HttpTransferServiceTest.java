package ru.gazprombank.middle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import ru.gazprombank.middle.dto.ErrorResponse;
import ru.gazprombank.middle.dto.TransferRequest;
import ru.gazprombank.middle.dto.TransferResponse;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.gazprombank.middle.util.ErrorMessages.INSUFFICIENT_FUNDS_ERROR;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class HttpTransferServiceTest {
    private WireMockServer wireMockServer;
    private HttpTransferService transferService;
    private TransferRequest request;

    private static final String TRANSFER_URL = "/v2/transfers";
    private static final int PORT = 8999;

    @Autowired
    public void setTransferService(HttpTransferService transferService) {
        this.transferService = transferService;
    }

    @BeforeEach
    void setUp() {
        request = new TransferRequest("user1", "user2", "100");
        wireMockServer = new WireMockServer(PORT);
        wireMockServer.start();
        WireMock.configureFor(PORT);
    }

    @AfterEach
    void tearDown() {
    wireMockServer.stop();
    }

    @Test
    void createTransfer() {
        String expectedTransferId = UUID.randomUUID().toString();
        stubFor(post(urlEqualTo(TRANSFER_URL))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody("{\"transferId\":\"" + expectedTransferId + "\"}")
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        TransferResponse response = transferService.transfer(request);

        assertEquals(expectedTransferId, response.transferId());
        assertNull(response.message());
    }


    @Test
    void insufficientFundsErrorReturnsErrorResponseWithFourFields() throws JsonProcessingException {
        ErrorResponse expectedError = new ErrorResponse(
                INSUFFICIENT_FUNDS_ERROR,
                "TransferError",
                "400",
                "123"
        );

        stubFor(post(urlEqualTo(TRANSFER_URL))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withBody(new ObjectMapper().writeValueAsString(expectedError))
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        TransferResponse response = transferService.transfer(request);

        assertNull(response.transferId());
        assertEquals(INSUFFICIENT_FUNDS_ERROR, response.message());
    }
}