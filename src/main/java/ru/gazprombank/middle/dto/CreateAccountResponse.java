package ru.gazprombank.middle.dto;

import org.springframework.http.HttpStatus;

import static ru.gazprombank.middle.util.ErrorMessages.SERVER_ERROR;

public record CreateAccountResponse(boolean success, String message) {
    public static HttpStatus determineStatus(String errorMsg) {
        if (errorMsg.equals(SERVER_ERROR)) {
        return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}