package ru.gazprombank.middle.repository;

import org.springframework.stereotype.Repository;
import ru.gazprombank.middle.dto.AccountDTO;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAccountRepositoryImpl implements AccountRepository {
    private final ConcurrentHashMap<Long, AccountDTO> accounts = new ConcurrentHashMap<>();

    @Override
    public void save(AccountDTO account) {
        accounts.put(account.userId(), account);
    }

    @Override
    public Optional<AccountDTO> findByUserId(Long userId) {
        return Optional.ofNullable(accounts.get(userId));
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return accounts.containsKey(userId);
    }
}
