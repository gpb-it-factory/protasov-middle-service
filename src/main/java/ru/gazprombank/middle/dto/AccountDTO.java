package ru.gazprombank.middle.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class AccountDTO {
    String accountId;
    String accountName;
    BigDecimal amount;
}