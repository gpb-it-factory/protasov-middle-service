package ru.gazprombank.middle.repository;

import ru.gazprombank.middle.dto.UserDTO;

import java.util.Optional;

public interface UserRepository {
    void save(UserDTO user);
    Optional<UserDTO> findById(Long userId);
    boolean existsById(Long userId);

    boolean existsByUsername(String username);
    Optional<UserDTO> findByUsername(String username);
}
