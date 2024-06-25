package ru.gazprombank.middle.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.gazprombank.middle.client.CreateAccountClient;
import ru.gazprombank.middle.dto.CreateAccountRequest;
import ru.gazprombank.middle.dto.CreateAccountResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static ru.gazprombank.middle.util.ErrorMessages.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTests {
    @Mock
    private CreateAccountClient createAccountClient;

    @InjectMocks
    private AccountServiceImpl accountService;

    private static final long USER_ID = 1L;
    private static final String ACCOUNT_NAME = "Акционный";

    @Test
    void whenCreateAccountWithValidData_thenReturnOk() {
        CreateAccountRequest request = new CreateAccountRequest(ACCOUNT_NAME);
        CreateAccountResponse response = new CreateAccountResponse(true, null);

        doReturn(response).when(createAccountClient).createAccount(USER_ID, request);

        ResponseEntity<?> result = accountService.createAccount(USER_ID, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void whenCreateAccountForNonRegisteredUser_thenReturnNotFound() {
        CreateAccountRequest request = new CreateAccountRequest(ACCOUNT_NAME);
        CreateAccountResponse response = new CreateAccountResponse(false, USER_NOT_REGISTERED_ERROR);

        doReturn(response).when(createAccountClient).createAccount(USER_ID, request);
        ResponseEntity<?> result = accountService.createAccount(USER_ID, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(USER_NOT_REGISTERED_ERROR);
    }
    @Test
    void whenCreateAccountForUserWithExistingAccount_thenReturnBadRequest() {
        CreateAccountRequest request = new CreateAccountRequest("Акционный");
        CreateAccountResponse response = new CreateAccountResponse(false, ACCOUNT_ALREADY_OPENED_ERROR);

        doReturn(response).when(createAccountClient).createAccount(USER_ID, request);

        ResponseEntity<?> result = accountService.createAccount(USER_ID, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(ACCOUNT_ALREADY_OPENED_ERROR);
    }
}
