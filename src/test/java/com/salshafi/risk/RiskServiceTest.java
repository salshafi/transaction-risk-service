package com.salshafi.risk;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RiskServiceTest {

    private static final Instant NOW = Instant.parse("2026-11-07T15:00:00Z");

    @Autowired
    private RiskService service;

    @Test
    void normalTransactionIsNotFlagged() {
        RiskAssessment result = service.assess(transaction("acc-1", "25.50", NOW));

        assertThat(result.flagged()).isFalse();
        assertThat(result.triggeredRules()).isEmpty();
    }

    @Test
    void largeAmountIsFlagged() {
        RiskAssessment result = service.assess(transaction("acc-1", "5000.00", NOW));

        assertThat(result.flagged()).isTrue();
        assertThat(result.triggeredRules()).containsExactly("HIGH_AMOUNT");
    }

    @Test
    void sixTransactionsInTenMinutesAreFlaggedAsHighVelocity() {
        RiskAssessment last = null;
        // Oldest first, one minute apart, so the last one is the newest.
        for (int i = 5; i >= 0; i--) {
            last = service.assess(transaction("acc-9", "10.00", NOW.minusSeconds(i * 60L)));
        }

        assertThat(last.flagged()).isTrue();
        assertThat(last.triggeredRules()).containsExactly("HIGH_VELOCITY");
    }

    private Transaction transaction(String accountId, String amount, Instant time) {
        return new Transaction(accountId, new BigDecimal(amount), "CAD", "CA", time);
    }
}