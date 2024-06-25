package ru.gazprombank.middle.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gazprombank.middle.dto.AccountDTO;
import ru.gazprombank.middle.dto.CreateAccountRequest;
import ru.gazprombank.middle.dto.CreateAccountResponse;
import ru.gazprombank.middle.repository.AccountRepository;
import ru.gazprombank.middle.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static ru.gazprombank.middle.util.ErrorMessages.ACCOUNT_ALREADY_OPENED_ERROR;
import static ru.gazprombank.middle.util.ErrorMessages.USER_NOT_REGISTERED_ERROR;

@Component
public class InMemoryCreateAccountClientImpl implements CreateAccountClient {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public InMemoryCreateAccountClientImpl(UserRepository userRepository,
                                           AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }
    @Override
    public CreateAccountResponse createAccount(Long id, CreateAccountRequest request) {
        return validateUserAndAccount(id)
                .map(errMessage -> new CreateAccountResponse(false, errMessage))
                .orElseGet(() -> createNewAccount(id, request));
    }

    private Optional<String> validateUserAndAccount(Long userId) {
        if (!userRepository.existsById(userId)) {
            return Optional.of(USER_NOT_REGISTERED_ERROR);
        }
        if (accountRepository.existsByUserId(userId)) {
            return Optional.of(ACCOUNT_ALREADY_OPENED_ERROR);
        }
        return Optional.empty();
    }
    private CreateAccountResponse createNewAccount(Long id, CreateAccountRequest request) {
        String accountId = UUID.randomUUID().toString();
        AccountDTO newAccount = new AccountDTO(accountId,
                id,
                request.accountName(),
                new BigDecimal("5000.00"));

        accountRepository.save(newAccount);

        return new CreateAccountResponse(true, null);
    }
}
