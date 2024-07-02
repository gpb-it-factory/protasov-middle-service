package ru.gazprombank.middle.dto;


import java.math.BigDecimal;

public record AccountDTO(String accountId, String accountName, BigDecimal amount) {
}