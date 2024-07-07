package ru.gazprombank.middle.service;

import ru.gazprombank.middle.dto.TransferRequest;
import ru.gazprombank.middle.dto.TransferResponse;

public interface TransferService {
    TransferResponse transfer(TransferRequest request);
}
