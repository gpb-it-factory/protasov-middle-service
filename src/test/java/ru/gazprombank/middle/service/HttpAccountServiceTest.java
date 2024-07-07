package ru.gazprombank.middle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import ru.gazprombank.middle.dto.*;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.gazprombank.middle.util.ErrorMessages.ACCOUNT_ALREADY_OPENED_ERROR;
import static ru.gazprombank.middle.util.ErrorMessages.USER_NOT_REGISTERED_ERROR;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class HttpAccountServiceTest {
    private WireMockServer wireMockServer;
    private HttpAccountService accountService;
    private CreateAccountRequest createAccountRequest;
    private Long userId;
    private static ObjectMapper objectMapper;

    private static final String CREATE_ACCOUNT_URL = "/v2/users/1/accounts";
    private static final String GET_BALANCE_URL = "/v2/users/1/accounts";
    private static final int PORT = 8999;
    private static final String ACCOUNT_NAME = "Акционный";

    @Autowired
    public void setAccountService(HttpAccountService accountService) {
        this.accountService = accountService;
    }

    @BeforeEach
    void setUp() {
        userId = 1L;
        objectMapper = new ObjectMapper();
        createAccountRequest = new CreateAccountRequest(ACCOUNT_NAME);
        wireMockServer = new WireMockServer(PORT);
        wireMockServer.start();
        WireMock.configureFor(PORT);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void createAccount() {
        stubFor(post(urlEqualTo(CREATE_ACCOUNT_URL))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())));

        CreateAccountResponse response = accountService.createAccount(userId, createAccountRequest);

        assertTrue(response.success());
        assertNull(response.message());
    }

    @Test
    void createAccountErrorWhenUserNotRegistered() throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse(USER_NOT_REGISTERED_ERROR,
                "Account", "120", "trace123");
        stubFor(post(urlEqualTo(CREATE_ACCOUNT_URL))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withBody(objectMapper.writeValueAsString(errorResponse))
                        .withHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE)));

        CreateAccountResponse response = accountService.createAccount(userId, createAccountRequest);

        assertFalse(response.success());
        assertEquals(USER_NOT_REGISTERED_ERROR, response.message());
    }

    @Test
    void createAccountErrorWhenAccountAlreadyOpen() throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse(ACCOUNT_ALREADY_OPENED_ERROR,
                "Account", "120", "trace123");
        stubFor(post(urlEqualTo(CREATE_ACCOUNT_URL))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withBody(objectMapper.writeValueAsString(errorResponse))
                        .withHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE)));

        CreateAccountResponse response = accountService.createAccount(userId, createAccountRequest);

        assertFalse(response.success());
        assertEquals(ACCOUNT_ALREADY_OPENED_ERROR, response.message());
    }

    @Test
    void getCurrentBalance() throws JsonProcessingException {
        List<AccountDTO> accounts = List.of(
                new AccountDTO("123", ACCOUNT_NAME, new BigDecimal("1000.00"))
        );
        stubFor(get(urlEqualTo(GET_BALANCE_URL))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(objectMapper.writeValueAsString(accounts))
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        CurrentBalanceResponse response = accountService.getCurrentBalance(userId);

        assertEquals(ACCOUNT_NAME, response.accounts().getFirst().getAccountName());
        assertEquals( new BigDecimal("1000.00"), response.accounts().getFirst().getAmount());
    }

    @Test
    void getCurrentBalanceErrorWhenUserNotFound() throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse(USER_NOT_REGISTERED_ERROR,
                "Account", "120", "trace123");
        stubFor(get(urlEqualTo(GET_BALANCE_URL))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withBody(objectMapper.writeValueAsString(errorResponse))
                        .withHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE)));

        CurrentBalanceResponse response = accountService.getCurrentBalance(userId);

        assertFalse(response.success());
        assertEquals(USER_NOT_REGISTERED_ERROR, response.message());
        assertNull(response.accounts());
    }
}
