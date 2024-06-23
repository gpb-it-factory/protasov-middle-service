package ru.gazprombank.middle.dto;

import org.springframework.http.HttpStatus;
import ru.gazprombank.middle.util.ErrorMessages;

public record UserRegistrationResponse(boolean success, String message) {
    public static HttpStatus determineStatus(String errorMsg) {
        if (errorMsg.equals(ErrorMessages.USER_ALREADY_EXISTS)
                || errorMsg.equals(ErrorMessages.REGISTRATION_ERROR)) {
            return HttpStatus.BAD_REQUEST;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
