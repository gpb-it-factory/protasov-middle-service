package ru.gazprombank.middle.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gazprombank.middle.dto.CreateAccountRequest;
import ru.gazprombank.middle.dto.UserRegistrationRequest;
import ru.gazprombank.middle.service.AccountService;
import ru.gazprombank.middle.service.RegistrationService;

@RestController
@RequestMapping("/api/v2/users")
public class UsersController {
    private final RegistrationService registrationService;
    private final AccountService accountService;

    @Autowired
    public UsersController(RegistrationService registrationService, AccountService accountService) {
        this.registrationService = registrationService;
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        return registrationService.registerUser(userRegistrationRequest);
    }

    @PostMapping( "/{id}/accounts")
    public ResponseEntity<?> createAccount(@PathVariable Long id,
                                           @Valid @RequestBody CreateAccountRequest createAccountRequest) {
        return accountService.createAccount(id, createAccountRequest);
    }
}
