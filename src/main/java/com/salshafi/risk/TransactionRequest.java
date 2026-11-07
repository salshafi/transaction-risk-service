package com.salshafi.risk;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * The JSON body a client sends to the API.
 * The annotations are the validation rules; bad input is rejected
 * before it ever reaches the risk rules.
 */
public record TransactionRequest(
        @NotBlank String accountId,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotBlank @Size(min = 2, max = 2) String country,
        @NotNull Instant occurredAt) {
}