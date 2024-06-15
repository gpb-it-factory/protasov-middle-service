package ru.gazprombank.middle.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.dto.UserRegistrationRequest;
import ru.gazprombank.middle.dto.UserCreation;
import ru.gazprombank.middle.repository.UserRepository;

@Component
public class InMemoryBackendClientImpl implements BackendClient {
    private final UserRepository userRepository;
    @Autowired
    public InMemoryBackendClientImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserRegistrationResponse createUser(UserRegistrationRequest userRegistrationRequest) {
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
