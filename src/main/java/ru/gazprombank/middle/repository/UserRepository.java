package ru.gazprombank.middle.repository;

import ru.gazprombank.middle.dto.UserCreation;

import java.util.Optional;

public interface UserRepository {
    void save(UserCreation user);
    Optional<UserCreation> findById(Long userId);
    boolean existsById(Long userId);
}
