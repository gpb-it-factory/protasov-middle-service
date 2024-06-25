package ru.gazprombank.middle.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.gazprombank.middle.client.UserRegistrationClient;
import ru.gazprombank.middle.dto.UserRegistrationRequest;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.util.ErrorMessages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTests {
    @Mock
    private UserRegistrationClient userRegistrationClient;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    public void whenRegistrationIsSuccessful_thenReturnOk() {
        var request = new UserRegistrationRequest(1L, "gpb01");
        doReturn(new UserRegistrationResponse(true, null))
                .when(userRegistrationClient).createUser(request);

        ResponseEntity<?> response = registrationService.registerUser(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void whenDuplicateRegistration_thenBadRequest() {
        var request = new UserRegistrationRequest(1L, "ex12");
        doReturn(new UserRegistrationResponse(false, ErrorMessages.USER_ALREADY_EXISTS_ERROR))
                .when(userRegistrationClient).createUser(request);

        ResponseEntity<?> response = registrationService.registerUser(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(ErrorMessages.USER_ALREADY_EXISTS_ERROR);
    }
}
