package ru.gazprombank.middle.client;

import org.springframework.stereotype.Component;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.dto.UserRegistrationRequest;
import ru.gazprombank.middle.dto.UserCreation;

import java.util.HashMap;

@Component
public class InMemoryBackendClientImpl implements BackendClient {
    HashMap<String, UserCreation> users;

    public InMemoryBackendClientImpl() {
        this.users = new HashMap<>();
    }
    @Override
    public UserRegistrationResponse createUser(UserRegistrationRequest userRegistrationRequest) {
        UserCreation userCreation = new UserCreation(userRegistrationRequest.userId(),
                userRegistrationRequest.userName());
        return validateAndRegisterUser(userCreation);
    }

    private UserRegistrationResponse validateAndRegisterUser(UserCreation userCreation) {
        String userName = userCreation.userName();
        if (userExists(userName)) {
            return new UserRegistrationResponse(false, "Такой пользователь уже существует.");
        }
        users.put(userName, userCreation);
        return new UserRegistrationResponse(true, null);

    }
    private boolean userExists(String userName) {
        return users.containsKey(userName);
    }
}
