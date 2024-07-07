package ru.gazprombank.middle.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gazprombank.middle.dto.TransferRequest;
import ru.gazprombank.middle.dto.TransferResponse;
import ru.gazprombank.middle.dto.TransferSuccessResponse;
import ru.gazprombank.middle.service.TransferService;

@RestController
@RequestMapping("/api/v2/transfers")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest transferRequest) {
        TransferResponse response = transferService.transfer(transferRequest);
        if (response.success()) {
            return ResponseEntity.ok(new TransferSuccessResponse(response.transferId()));
        } else {
            HttpStatus status = TransferResponse.determineStatus(response.message());
            return ResponseEntity.status(status).body(response.message());
        }
    }
}