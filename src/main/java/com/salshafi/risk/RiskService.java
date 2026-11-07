package com.salshafi.risk;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Saves a transaction, runs every risk rule on it and reports what fired.
 */
@Service
public class RiskService {

    // Hard-coded for now. Later these could move into application.properties.
    private static final BigDecimal HIGH_AMOUNT_LIMIT = new BigDecimal("1000.00");
    private static final int MAX_TRANSACTIONS = 5;
    private static final Duration VELOCITY_WINDOW = Duration.ofMinutes(10);

    private final TransactionRepository repository;
    private final List<RiskRule> rules;

    // Spring passes in the repository for us (dependency injection).
    public RiskService(TransactionRepository repository) {
        this.repository = repository;
        this.rules = List.of(
                new HighAmountRule(HIGH_AMOUNT_LIMIT),
                new VelocityRule(repository, MAX_TRANSACTIONS, VELOCITY_WINDOW));
    }

    @Transactional
    public RiskAssessment assess(Transaction transaction) {
        // Save first: the velocity rule counts this transaction too.
        Transaction saved = repository.save(transaction);

        List<String> triggered = rules.stream()
                .filter(rule -> rule.isTriggered(saved))
                .map(RiskRule::name)
                .toList();

        return new RiskAssessment(saved.getId(), !triggered.isEmpty(), triggered);
    }
}