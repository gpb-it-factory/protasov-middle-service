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
        Optional<UserDTO> toUser = userRepository.findByUsername(request.to());

        return validateUsers(fromUser, toUser)
                .or(() -> validateAccounts(fromUser.get(), toUser.get()))
                .orElseGet(() -> executeTransfer(fromUser.get().userId(),
                        toUser.get().userId(), request.amount()));
    }

    private Optional<TransferResponse> validateUsers(Optional<UserDTO> fromUser, Optional<UserDTO> toUser) {
        if (fromUser.isEmpty()) {
            return Optional.of(new TransferResponse(false, null, USER_NOT_REGISTERED_ERROR));
        }

        if (toUser.isEmpty()) {
            return Optional.of(new TransferResponse(false, null, RECIPIENT_NOT_FOUND_ERROR));
        }

        if (fromUser.get().userId().equals(toUser.get().userId())) {
            return Optional.of(new TransferResponse(false, null, SELF_TRANSFER_ERROR));
        }

        return Optional.empty();
    }

    private Optional<TransferResponse> validateAccounts(UserDTO fromUser, UserDTO toUser) {
        List<AccountDTO> fromAccounts = accountRepository.findByUserId(fromUser.userId());
        if (fromAccounts.isEmpty()) {
            return Optional.of(new TransferResponse(false, null, NO_ACCOUNT_ERROR));
        }

        List<AccountDTO> toAccounts = accountRepository.findByUserId(toUser.userId());
        if (toAccounts.isEmpty()) {
            return Optional.of(new TransferResponse(false, null, RECIPIENT_NO_ACCOUNT_ERROR));
        }

        return Optional.empty();
    }


    private TransferResponse executeTransfer(Long fromUserId, Long toUserId, String amount) {
        BigDecimal transferAmount = new BigDecimal(amount);

        AccountDTO fromAccount = accountRepository.findByUserId(fromUserId).getFirst();
        AccountDTO toAccount = accountRepository.findByUserId(toUserId).getFirst();

        BigDecimal fromBalance = fromAccount.getAmount();
        if (fromBalance.compareTo(transferAmount) < 0) {
            return new TransferResponse(false, null, INSUFFICIENT_FUNDS_ERROR);
        }

        BigDecimal newFromBalance = fromBalance.subtract(transferAmount);
        accountRepository.update(fromUserId, fromAccount.getAccountId(), newFromBalance);

        BigDecimal newToBalance = toAccount.getAmount().add(transferAmount);
        accountRepository.update(toUserId, toAccount.getAccountId(), newToBalance);

        String transferId = UUID.randomUUID().toString();
        return new TransferResponse(true, transferId, null);
    }
}