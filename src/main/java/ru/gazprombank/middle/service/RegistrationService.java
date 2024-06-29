package ru.gazprombank.middle.service;

import ru.gazprombank.middle.dto.UserRegistrationRequest;
import ru.gazprombank.middle.dto.UserRegistrationResponse;

public interface RegistrationService {
    UserRegistrationResponse registerUser(UserRegistrationRequest request);
}
