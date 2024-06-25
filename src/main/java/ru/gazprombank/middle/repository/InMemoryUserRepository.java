package ru.gazprombank.middle.repository;

import org.springframework.stereotype.Repository;
import ru.gazprombank.middle.dto.UserDTO;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final ConcurrentHashMap<Long, UserDTO> users = new ConcurrentHashMap<>();

    @Override
    public void save(UserDTO user) {
        users.put(user.userId(), user);
    }

    @Override
    public Optional<UserDTO> findById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public boolean existsById(Long userId) {
        return users.containsKey(userId);
    }
}