package com.salshafi.risk;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// @Transactional rolls the database back after each test,
// so tests never leave data behind for each other.
@SpringBootTest
@Transactional
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    void savesAndLoadsATransaction() {
        Transaction saved = repository.save(newTransaction("acc-1"));

        assertThat(saved.getId()).isNotNull();

        Transaction loaded = repository.findById(saved.getId()).orElseThrow();
        assertThat(loaded.getAccountId()).isEqualTo("acc-1");
        assertThat(loaded.getAmount()).isEqualByComparingTo("25.50");
    }

    @Test
    void findsTransactionsByAccountId() {
        repository.save(newTransaction("acc-1"));
        repository.save(newTransaction("acc-1"));
        repository.save(newTransaction("acc-2"));

        assertThat(repository.findByAccountId("acc-1")).hasSize(2);
    }

    // Small helper so each test doesn't repeat the same setup.
    private Transaction newTransaction(String accountId) {
        return new Transaction(accountId, new BigDecimal("25.50"), "CAD", "CA", Instant.now());
    }
}