package com.salshafi.risk;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;

// Plain unit test: no Spring and no database, so it runs in milliseconds.
class HighAmountRuleTest {

    private final HighAmountRule rule = new HighAmountRule(new BigDecimal("1000.00"));

    @Test
    void flagsAnAmountAboveTheLimit() {
        assertThat(rule.isTriggered(transactionOf("1000.01"))).isTrue();
    }

    // "Over the limit" means strictly greater, so exactly 1000.00 is fine.
    @Test
    void doesNotFlagAnAmountExactlyAtTheLimit() {
        assertThat(rule.isTriggered(transactionOf("1000.00"))).isFalse();
    }

    @Test
    void doesNotFlagASmallAmount() {
        assertThat(rule.isTriggered(transactionOf("25.50"))).isFalse();
    }

    private Transaction transactionOf(String amount) {
        return new Transaction("acc-1", new BigDecimal(amount), "CAD", "CA", Instant.now());
    }
}