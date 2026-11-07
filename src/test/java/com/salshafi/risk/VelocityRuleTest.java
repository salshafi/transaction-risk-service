package com.salshafi.risk;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// This rule talks to the database, so unlike HighAmountRuleTest
// it needs Spring. @Transactional undoes the data after each test.
@SpringBootTest
@Transactional
class VelocityRuleTest {

    private static final Instant NOW = Instant.parse("2026-11-07T15:00:00Z");

    @Autowired
    private TransactionRepository repository;

    @Test
    void flagsMoreThanFiveTransactionsInTenMinutes() {
        Transaction newest = saveInsideTheWindow("acc-1", 6);

        assertThat(rule().isTriggered(newest)).isTrue();
    }

    @Test
    void doesNotFlagExactlyFiveTransactions() {
        Transaction newest = saveInsideTheWindow("acc-1", 5);

        assertThat(rule().isTriggered(newest)).isFalse();
    }

    @Test
    void ignoresOldTransactionsAndOtherAccounts() {
        // Five recent ones for acc-1, which is allowed...
        Transaction newest = saveInsideTheWindow("acc-1", 5);
        // ...plus noise that must not be counted:
        save("acc-1", NOW.minus(Duration.ofHours(1)));   // too old
        save("acc-2", NOW.minusSeconds(30));             // different account
        save("acc-2", NOW.minusSeconds(90));

        assertThat(rule().isTriggered(newest)).isFalse();
    }

    private VelocityRule rule() {
        return new VelocityRule(repository, 5, Duration.ofMinutes(10));
    }

    // Saves transactions one minute apart, oldest first, and returns the
    // newest one (at NOW). The rule looks backward from the transaction it
    // checks, so the one we pass in has to be the latest.
    private Transaction saveInsideTheWindow(String accountId, int howMany) {
        Transaction newest = null;
        for (int i = howMany - 1; i >= 0; i--) {
            newest = save(accountId, NOW.minusSeconds(i * 60L));
        }
        return newest;
    }

    private Transaction save(String accountId, Instant time) {
        return repository.save(
                new Transaction(accountId, new BigDecimal("10.00"), "CAD", "CA", time));
    }
}