package com.salshafi.risk;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * One payment transaction that the service receives and checks.
 * Each object is stored as a row in the "transactions" table.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    // The database generates the id, so we never set it ourselves.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accountId;

    // BigDecimal instead of double, because floating point can
    // introduce small rounding errors and money has to stay exact.
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    // 3-letter code like CAD or USD
    @Column(nullable = false, length = 3)
    private String currency;

    // 2-letter country code like CA or US
    @Column(nullable = false, length = 2)
    private String country;

    // Stored as an Instant (a point in UTC time) so time zones
    // don't cause confusion when we compare transactions later.
    @Column(nullable = false)
    private Instant occurredAt;

    // JPA needs an empty constructor to build objects from database rows.
    protected Transaction() {
    }

    public Transaction(String accountId, BigDecimal amount, String currency,
                       String country, Instant occurredAt) {
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
        this.country = country;
        this.occurredAt = occurredAt;
    }

    public Long getId() { return id; }
    public String getAccountId() { return accountId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getCountry() { return country; }
    public Instant getOccurredAt() { return occurredAt; }
}