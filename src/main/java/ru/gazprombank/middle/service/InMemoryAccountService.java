package ru.gazprombank.middle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.gazprombank.middle.dto.AccountDTO;
import ru.gazprombank.middle.dto.CreateAccountRequest;
import ru.gazprombank.middle.dto.CreateAccountResponse;
import ru.gazprombank.middle.dto.CurrentBalanceResponse;
import ru.gazprombank.middle.repository.AccountRepository;
import ru.gazprombank.middle.repository.UserRepository;

import java.util.List;
import java.util.UUID;

import static ru.gazprombank.middle.util.ErrorMessages.ACCOUNT_ALREADY_OPENED_ERROR;
import static ru.gazprombank.middle.util.ErrorMessages.USER_NOT_REGISTERED_ERROR;

@Component
@ConditionalOnProperty(name = "backend.client-type", havingValue = "inMemory")
public class InMemoryAccountService implements AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public InMemoryAccountService(UserRepository userRepository,
                                  AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public CreateAccountResponse createAccount(Long id, CreateAccountRequest request) {
        if (!userRepository.existsById(id)) {
            return new CreateAccountResponse(false, USER_NOT_REGISTERED_ERROR);
        }
        if (accountRepository.existsByUserId(id)) {
            return new CreateAccountResponse(false, ACCOUNT_ALREADY_OPENED_ERROR);
        }

        String accountId = UUID.randomUUID().toString();
        AccountDTO newAccount = new AccountDTO(accountId,
                request.accountName(),
                "5000.00");

        accountRepository.save(id, newAccount);

        return new CreateAccountResponse(true, null);
    }

    @Override
    public CurrentBalanceResponse getCurrentBalance(Long id) {
        if (!userRepository.existsById(id)) {
            return new CurrentBalanceResponse(
                    false, null, USER_NOT_REGISTERED_ERROR);
        }

        List<AccountDTO> accounts = accountRepository.findByUserId(id);

        return new CurrentBalanceResponse(true, accounts, null);
    }
}