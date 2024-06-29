package ru.gazprombank.middle.service;

import ru.gazprombank.middle.dto.CreateAccountRequest;
import ru.gazprombank.middle.dto.CreateAccountResponse;

public interface AccountService {
    CreateAccountResponse createAccount(Long id, CreateAccountRequest request);
}

