package ru.gazprombank.middle.service;

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
import ru.gazprombank.middle.dto.UserRegistrationRequest;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.util.ErrorMessages;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class HttpRegistrationServiceTests {
    private WireMockServer wireMockServer;

    private HttpRegistrationService registrationService;
    UserRegistrationRequest request;

    private static final String REGISTRATION_URL = "/v2/users";
    private static final int PORT = 8999;

    @Autowired
    public void setRegistrationService(HttpRegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    @BeforeEach
    void setUp() {
        request = new UserRegistrationRequest(1L, "ex12");
        wireMockServer = new WireMockServer(PORT);
        wireMockServer.start();
        WireMock.configureFor(PORT);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void registerNewUser() {
        stubFor(post(urlEqualTo(REGISTRATION_URL))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())));


        UserRegistrationResponse response = registrationService.registerUser(request);

        assertTrue(response.success());
        assertNull(response.message());
    }

    @Test
    void registerUserErrorWhenAlreadyRegistered() {
        stubFor(post(urlEqualTo(REGISTRATION_URL))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.CONFLICT.value())
                        .withBody(ErrorMessages.USER_ALREADY_EXISTS)
                        .withHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE)));

        UserRegistrationResponse response = registrationService.registerUser(request);

        assertEquals(ErrorMessages.USER_ALREADY_EXISTS, response.message());
    }
}