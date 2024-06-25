package ru.gazprombank.middle.repository;

import ru.gazprombank.middle.dto.AccountDTO;

import java.util.Optional;

public interface AccountRepository {
    void save(AccountDTO account);
    Optional<AccountDTO> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
