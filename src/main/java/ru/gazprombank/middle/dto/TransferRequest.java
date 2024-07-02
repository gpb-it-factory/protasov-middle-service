package ru.gazprombank.middle.dto;

import jakarta.validation.constraints.NotBlank;

public record TransferRequest(
        @NotBlank String from,
        @NotBlank String to,
        @NotBlank String amount
) {}
