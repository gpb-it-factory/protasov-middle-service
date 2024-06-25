package ru.gazprombank.middle.client;

import ru.gazprombank.middle.dto.CreateAccountRequest;
import ru.gazprombank.middle.dto.CreateAccountResponse;

public interface CreateAccountClient {
    CreateAccountResponse createAccount(Long id, CreateAccountRequest request);
}
