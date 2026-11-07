package com.salshafi.risk;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;

// Plain unit test of the validation rules, so no Spring is needed.
class TransactionRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void aValidRequestHasNoViolations() {
        TransactionRequest request = request("acc-1", "25.50");

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void rejectsAnAmountOfZeroOrLess() {
        assertThat(validator.validate(request("acc-1", "0.00"))).isNotEmpty();
        assertThat(validator.validate(request("acc-1", "-5.00"))).isNotEmpty();
    }

    @Test
    void rejectsABlankAccountId() {
        assertThat(validator.validate(request("", "25.50"))).isNotEmpty();
    }

    private TransactionRequest request(String accountId, String amount) {
        return new TransactionRequest(accountId, new BigDecimal(amount), "CAD", "CA", Instant.now());
    }
}