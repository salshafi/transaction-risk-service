package com.salshafi.risk;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Receives transactions over HTTP and returns the risk result.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final RiskService service;

    public TransactionController(RiskService service) {
        this.service = service;
    }

    // @Valid makes Spring check the annotations on TransactionRequest.
    // If any fail, Spring answers 400 and this method never runs.
    @PostMapping
    public RiskAssessment assess(@Valid @RequestBody TransactionRequest request) {
        Transaction transaction = new Transaction(
                request.accountId(),
                request.amount(),
                request.currency(),
                request.country(),
                request.occurredAt());

        return service.assess(transaction);
    }
}