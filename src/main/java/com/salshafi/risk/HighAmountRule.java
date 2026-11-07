package com.salshafi.risk;

import java.math.BigDecimal;

/**
 * Flags any transaction whose amount is above a set limit.
 */
public class HighAmountRule implements RiskRule {

    private final BigDecimal threshold;

    // The limit is passed in, not hard-coded, so it can be changed
    // later through configuration and is easy to test.
    public HighAmountRule(BigDecimal threshold) {
        this.threshold = threshold;
    }

    @Override
    public String name() {
        return "HIGH_AMOUNT";
    }

    @Override
    public boolean isTriggered(Transaction transaction) {
        // compareTo instead of equals: BigDecimal("100.0") and ("100.00")
        // are the same number but equals() would say they differ.
        return transaction.getAmount().compareTo(threshold) > 0;
    }
}