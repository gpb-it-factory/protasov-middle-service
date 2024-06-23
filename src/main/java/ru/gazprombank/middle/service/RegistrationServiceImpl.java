package ru.gazprombank.middle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gazprombank.middle.client.BackendClient;
import ru.gazprombank.middle.dto.UserRegistrationResponse;
import ru.gazprombank.middle.dto.UserRegistrationRequest;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final BackendClient backendClient;
    @Autowired
    public RegistrationServiceImpl(BackendClient backendClient) {
        this.backendClient = backendClient;
    }

    @Override
    public ResponseEntity<?> registerUser(UserRegistrationRequest userRegistrationRequest) {
        UserRegistrationResponse response = backendClient.createUser(userRegistrationRequest);
        if (response.success()) {
            return ResponseEntity.ok().build();
        } else {
            String message = response.message();
            HttpStatus status = UserRegistrationResponse.determineStatus(message);
            return ResponseEntity.status(status).body(message);
        }
    }
}
