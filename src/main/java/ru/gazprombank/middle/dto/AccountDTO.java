package ru.gazprombank.middle.dto;

import java.math.BigDecimal;

public record AccountDTO(String accountId, Long userId, String accountName, BigDecimal balance) {
}