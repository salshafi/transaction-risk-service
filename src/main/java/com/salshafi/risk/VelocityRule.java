package com.salshafi.risk;

import java.time.Duration;
import java.time.Instant;

/**
 * Flags an account that makes too many transactions in a short time,
 * for example more than 5 in 10 minutes.
 */
public class VelocityRule implements RiskRule {

    private final TransactionRepository repository;
    private final int maxTransactions;
    private final Duration window;

    public VelocityRule(TransactionRepository repository, int maxTransactions, Duration window) {
        this.repository = repository;
        this.maxTransactions = maxTransactions;
        this.window = window;
    }

    @Override
    public String name() {
        return "HIGH_VELOCITY";
    }

    @Override
    public boolean isTriggered(Transaction transaction) {
        // Look back from this transaction's own time, not from "now",
        // so the result is the same whenever the check runs.
        Instant end = transaction.getOccurredAt();
        Instant start = end.minus(window);

        // This assumes the transaction is already saved, so the count includes it.
        long count = repository.countByAccountIdAndOccurredAtBetween(
                transaction.getAccountId(), start, end);

        return count > maxTransactions;
    }
}