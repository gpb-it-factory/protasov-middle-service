package ru.gazprombank.middle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.gazprombank.middle.dto.UserDTO;
import ru.gazprombank.middle.dto.UserRegistrationRequest;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.repository.UserRepository;

@Service
@ConditionalOnProperty(name = "backend.client-type", havingValue = "inMemory")
public class InMemoryRegistrationService implements RegistrationService {
    private final UserRepository userRepository;
    @Autowired
    public InMemoryRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
        UserDTO userDTO = new UserDTO(userRegistrationRequest.userId(),
                userRegistrationRequest.userName());
        return validateAndRegisterUser(userDTO);
    }

    private UserRegistrationResponse validateAndRegisterUser(UserDTO userDTO) {
        if (userRepository.existsById(userDTO.userId())) {
            return new UserRegistrationResponse(false, "Такой пользователь уже существует.");
        }
        userRepository.save(userDTO);
        return new UserRegistrationResponse(true, null);
    }
}
