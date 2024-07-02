package ru.gazprombank.middle.dto;

import org.springframework.http.HttpStatus;
import ru.gazprombank.middle.util.ErrorMessages;

public record TransferResponse(boolean success, String transferId, String message) {
    public static HttpStatus determineStatus(String errorMsg) {
        if (errorMsg.equals(ErrorMessages.SERVER_ERROR)) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}