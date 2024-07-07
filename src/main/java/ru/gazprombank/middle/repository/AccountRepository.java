package ru.gazprombank.middle.repository;

import ru.gazprombank.middle.dto.AccountDTO;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository {
    void save(Long userId, AccountDTO account);
    List<AccountDTO> findByUserId(Long userId);
    boolean existsByUserId(Long userId);

    void update(Long userId, String accountId, BigDecimal newAmount) ;
}