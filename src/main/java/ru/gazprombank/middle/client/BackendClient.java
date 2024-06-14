package ru.gazprombank.middle.client;

import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.dto.UserRegistrationRequest;

public interface BackendClient {
    UserRegistrationResponse createUser(UserRegistrationRequest request);
}
