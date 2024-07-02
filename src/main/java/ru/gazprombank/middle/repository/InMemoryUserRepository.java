package ru.gazprombank.middle.repository;

import org.springframework.stereotype.Repository;
import ru.gazprombank.middle.dto.UserDTO;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
@Repository
public class InMemoryUserRepository implements UserRepository {
    private final ConcurrentHashMap<Long, String> usersById = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> usersByUsername = new ConcurrentHashMap<>();


    @Override
    public void save(UserDTO user) {
        usersById.put(user.userId(), user.userName());
        usersByUsername.put(user.userName(), user.userId());
    }
    @Override
    public Optional<UserDTO> findById(Long userId) {
        String username = usersById.get(userId);
        return Optional.ofNullable(username)
                .map(name -> new UserDTO(userId, name));
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        Long userId = usersByUsername.get(username);
        return Optional.ofNullable(userId)
                .map(id -> new UserDTO(id, username));
    }

    @Override
    public boolean existsById(Long userId) {
        return usersById.containsKey(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return usersByUsername.containsKey(username);
    }
}