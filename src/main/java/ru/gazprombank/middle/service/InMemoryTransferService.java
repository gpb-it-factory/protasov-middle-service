package ru.gazprombank.middle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.gazprombank.middle.dto.TransferRequest;
import ru.gazprombank.middle.dto.TransferResponse;
import ru.gazprombank.middle.dto.UserDTO;
import ru.gazprombank.middle.repository.AccountRepository;
import ru.gazprombank.middle.repository.UserRepository;
import ru.gazprombank.middle.dto.AccountDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.gazprombank.middle.util.ErrorMessages.*;

@Service
@ConditionalOnProperty(name = "backend.client-type", havingValue = "inMemory")
public class InMemoryTransferService implements TransferService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public InMemoryTransferService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TransferResponse transfer(TransferRequest request) {
        Optional<UserDTO> fromUser = userRepository.findByUsername(request.from());
        if (fromUser.isEmpty()) {
            return new TransferResponse(false, null, USER_NOT_REGISTERED_ERROR);
        }

        Optional<UserDTO> toUser = userRepository.findByUsername(request.to());
        if (toUser.isEmpty()) {
            return new TransferResponse(false, null, RECIPIENT_NOT_FOUND_ERROR);
        }

        Long fromUserId = fromUser.get().userId();
        Long toUserId = toUser.get().userId();

        if (fromUserId.equals(toUserId)) {
            return new TransferResponse(false, null, SELF_TRANSFER_ERROR);
        }

        List<AccountDTO> fromAccounts = accountRepository.findByUserId(fromUserId);
        if (fromAccounts.isEmpty()) {
            return new TransferResponse(false, null,
                    NO_ACCOUNT_ERROR);
        }

        List<AccountDTO> toAccounts = accountRepository.findByUserId(toUserId);
        if (toAccounts.isEmpty()) {
            return new TransferResponse(false, null,
                    RECIPIENT_NO_ACCOUNT_ERROR);
        }

        AccountDTO fromAccount = fromAccounts.getFirst();

        BigDecimal fromBalance = fromAccount.getAmount();
        BigDecimal transferAmount = new BigDecimal(request.amount());

        if (fromBalance.compareTo(transferAmount) < 0) {
            return new TransferResponse(false, null,
                    INSUFFICIENT_FUNDS_ERROR);
        }

        AccountDTO toAccount = toAccounts.getFirst();

        BigDecimal newFromBalance = fromBalance.subtract(transferAmount);

        accountRepository.update(fromUserId, fromAccount.getAccountId(), newFromBalance);


        BigDecimal newToBalance = toAccount.getAmount().add(transferAmount);
        accountRepository.update(toUserId, toAccount.getAccountId(), newToBalance);

        String transferId = UUID.randomUUID().toString();
        return new TransferResponse(true, transferId, null);
    }
}