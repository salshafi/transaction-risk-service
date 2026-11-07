package com.salshafi.risk;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Handles saving and loading Transaction rows.
 * We only write the interface; Spring generates the actual code at startup.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Spring reads the method name and builds the query for us:
    // SELECT * FROM transactions WHERE account_id = ?
    List<Transaction> findByAccountId(String accountId);

    // Counts one account's transactions between two moments (both ends included).
    long countByAccountIdAndOccurredAtBetween(String accountId, Instant start, Instant end);
}