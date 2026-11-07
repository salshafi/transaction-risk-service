package com.salshafi.risk;

/**
 * One check that decides whether a transaction looks risky.
 * Every rule in the service implements this same interface.
 */
public interface RiskRule {

    // Short label saved with the result when this rule fires.
    String name();

    boolean isTriggered(Transaction transaction);
}