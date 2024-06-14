package ru.gazprombank.middle.service;

import org.springframework.http.ResponseEntity;
import ru.gazprombank.middle.dto.UserRegistrationRequest;

public interface RegistrationService {
    ResponseEntity<?> registerUser(UserRegistrationRequest request);
}
