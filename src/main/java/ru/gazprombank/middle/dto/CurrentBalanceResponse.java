package ru.gazprombank.middle.dto;

import java.util.List;

public record CurrentBalanceResponse(boolean success, List<AccountDTO> accounts, String errorMessage) {
}