package ru.gazprombank.middle.dto;

public record ErrorResponse(String message, String type, String code, String traceId) {
}