package ru.gazprombank.middle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gazprombank.middle.client.CreateAccountClient;
import ru.gazprombank.middle.dto.CreateAccountRequest;
import ru.gazprombank.middle.dto.CreateAccountResponse;

@Service
public class AccountServiceImpl implements AccountService {
    private final CreateAccountClient createAccountClient;

    @Autowired
    public AccountServiceImpl(CreateAccountClient createAccountClient) {
        this.createAccountClient = createAccountClient;
    }
    @Override
    public ResponseEntity<?> createAccount(Long id, CreateAccountRequest request) {
        CreateAccountResponse response = createAccountClient.createAccount(id, request);
        if (response.success()) {
            return ResponseEntity.ok().build();
        } else {
            String message = response.message();
            HttpStatus status = CreateAccountResponse.determineStatus(message);
            return ResponseEntity.status(status).body(message);
        }
    }
}
