package com.salshafi.risk;

import java.util.List;

/**
 * The result of checking one transaction: whether it was flagged
 * and the names of the rules that fired.
 * A record is a short way to write a class that only holds data.
 */
public record RiskAssessment(Long transactionId, boolean flagged, List<String> triggeredRules) {
}