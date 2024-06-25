package ru.gazprombank.middle.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.dto.UserRegistrationRequest;
import ru.gazprombank.middle.dto.UserDTO;
import ru.gazprombank.middle.repository.UserRepository;

@Component
public class InMemoryUserRegistrationClientImpl implements UserRegistrationClient {
    private final UserRepository userRepository;
    @Autowired
    public InMemoryUserRegistrationClientImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserRegistrationResponse createUser(UserRegistrationRequest userRegistrationRequest) {
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
