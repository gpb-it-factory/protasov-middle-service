package ru.gazprombank.middle.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

import static ru.gazprombank.middle.util.ErrorMessages.SERVER_ERROR;

public record CurrentBalanceResponse(boolean success, List<AccountDTO> accounts, String message) {
    public static HttpStatus determineStatus(String errorMsg) {
        if (errorMsg.equals(SERVER_ERROR)) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}