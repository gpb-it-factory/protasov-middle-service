package ru.gazprombank.middle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gazprombank.middle.dto.UserCreation;
import ru.gazprombank.middle.dto.UserRegistrationRequest;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.repository.UserRepository;

@Service
public class InMemoryRegistrationService implements RegistrationService {
    private final UserRepository userRepository;
    @Autowired
    public InMemoryRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
        UserCreation userCreation = new UserCreation(userRegistrationRequest.userId(),
                userRegistrationRequest.userName());
        return validateAndRegisterUser(userCreation);
    }

    private UserRegistrationResponse validateAndRegisterUser(UserCreation userCreation) {
        if (userRepository.existsById(userCreation.userId())) {
            return new UserRegistrationResponse(false, "Такой пользователь уже существует.");
        }
        userRepository.save(userCreation);
        return new UserRegistrationResponse(true, null);
    }
}
