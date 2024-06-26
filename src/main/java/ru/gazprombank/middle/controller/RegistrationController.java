package ru.gazprombank.middle.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gazprombank.middle.dto.UserRegistrationRequest;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.service.RegistrationService;

@RestController
@RequestMapping("/api")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
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
}
