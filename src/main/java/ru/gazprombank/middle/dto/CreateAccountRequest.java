package ru.gazprombank.middle.dto;

import jakarta.validation.constraints.NotBlank;
public record CreateAccountRequest(@NotBlank String accountName) {
}
