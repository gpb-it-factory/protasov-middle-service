package ru.gazprombank.middle.service;

import org.springframework.http.ResponseEntity;
import ru.gazprombank.middle.dto.CreateAccountRequest;

public interface AccountService {
    ResponseEntity<?> createAccount(Long id, CreateAccountRequest request);
}
