package ru.gazprombank.middle.dto;

import jakarta.validation.constraints.NotNull;

public record UserRegistrationRequest(@NotNull Long userId, @NotNull String userName) { }