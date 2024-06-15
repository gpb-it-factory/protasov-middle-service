package ru.gazprombank.middle.repository;

import org.springframework.stereotype.Repository;
import ru.gazprombank.middle.dto.UserCreation;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
@Repository
public class InMemoryUserRepository implements UserRepository {
    private final ConcurrentHashMap<Long, UserCreation> users = new ConcurrentHashMap<>();

    @Override
    public void save(UserCreation user) {
        users.put(user.userId(), user);
    }

    @Override
    public Optional<UserCreation> findById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public boolean existsById(Long userId) {
        return users.containsKey(userId);
    }
}