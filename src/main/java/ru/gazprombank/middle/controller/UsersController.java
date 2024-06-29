package ru.gazprombank.middle.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gazprombank.middle.dto.CreateAccountRequest;
import ru.gazprombank.middle.dto.CreateAccountResponse;
import ru.gazprombank.middle.dto.UserRegistrationRequest;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.service.AccountService;
import ru.gazprombank.middle.service.RegistrationService;

@RestController
@RequestMapping("/api/v2/users")
public class UsersController {
    private final RegistrationService registrationService;
    private final AccountService accountService;

    public UsersController(RegistrationService registrationService, AccountService accountService) {
        this.registrationService = registrationService;
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserRegistrationResponse response = registrationService.registerUser(userRegistrationRequest);
        if (response.success()) {
            return ResponseEntity.ok().build();
        } else {
            String message = response.message();
            HttpStatus status = UserRegistrationResponse.determineStatus(message);
            return ResponseEntity.status(status).body(message);
        }
    }

    @PostMapping( "/{id}/accounts")
    public ResponseEntity<?> createAccount(@PathVariable Long id,
                                           @Valid @RequestBody CreateAccountRequest createAccountRequest) {
        CreateAccountResponse response = accountService.createAccount(id, createAccountRequest);

        if (response.success()) {
            return ResponseEntity.ok().build();
        } else {
            String message = response.message();
            HttpStatus status = CreateAccountResponse.determineStatus(message);
            return ResponseEntity.status(status).body(message);
        }
    }
}