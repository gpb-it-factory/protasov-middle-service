package ru.gazprombank.middle.service;

import ru.gazprombank.middle.dto.CreateAccountRequest;
import ru.gazprombank.middle.dto.CreateAccountResponse;
import ru.gazprombank.middle.dto.CurrentBalanceResponse;

public interface AccountService {
    CreateAccountResponse createAccount(Long id, CreateAccountRequest request);
    CurrentBalanceResponse getCurrentBalance(Long id);
}

