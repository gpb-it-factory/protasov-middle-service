package ru.gazprombank.middle.client;

import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.dto.UserRegistrationRequest;

public interface UserRegistrationClient {
    UserRegistrationResponse createUser(UserRegistrationRequest request);
}
