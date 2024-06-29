package ru.gazprombank.middle.repository;

import org.springframework.stereotype.Repository;
import ru.gazprombank.middle.dto.AccountDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAccountRepository implements AccountRepository {
    private final ConcurrentHashMap<Long, List<AccountDTO>> accounts = new ConcurrentHashMap<>();

    @Override
    public void save(Long userId, AccountDTO account) {
        accounts.computeIfAbsent(userId, k -> new ArrayList<>()).add(account);
    }
    @Override
    public List<AccountDTO> findByUserId(Long userId) {
        return accounts.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return accounts.containsKey(userId);
    }
}