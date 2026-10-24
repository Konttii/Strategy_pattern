package org.example.model;

import java.util.Locale;

/**
 * Immutable data carrier representing the result of a payment gateway execution.
 */
public class PaymentReceipt {
    private final String transactionId;
    private final String gatewayName;
    private final double baseAmountKzt;
    private final double processingFeeKzt;
    private final double totalChargedKzt;
    private final String authorizationDetails;

    public PaymentReceipt(
            String transactionId,
            String gatewayName,
            double baseAmountKzt,
            double processingFeeKzt,
            String authorizationDetails
    ) {
        if (transactionId == null || transactionId.isBlank()) {
            throw new IllegalArgumentException("Transaction ID must not be empty.");
        }
        if (gatewayName == null || gatewayName.isBlank()) {
            throw new IllegalArgumentException("Gateway name must not be empty.");
        }
        if (baseAmountKzt <= 0) {
            throw new IllegalArgumentException("Base payment amount must be strictly positive.");
        }
        if (processingFeeKzt < 0) {
            throw new IllegalArgumentException("Processing fee cannot be negative.");
        }
        this.transactionId = transactionId;
        this.gatewayName = gatewayName;
        this.baseAmountKzt = baseAmountKzt;
        this.processingFeeKzt = processingFeeKzt;
        this.totalChargedKzt = Math.round((baseAmountKzt + processingFeeKzt) * 100.0) / 100.0;
        this.authorizationDetails = authorizationDetails;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public double getBaseAmountKzt() {
        return baseAmountKzt;
    }

    public double getProcessingFeeKzt() {
        return processingFeeKzt;
    }

    public double getTotalChargedKzt() {
        return totalChargedKzt;
    }

    public String getAuthorizationDetails() {
        return authorizationDetails;
    }

    @Override
    public String toString() {
        return String.format(
                Locale.US,
                "Receipt[TX: %s | Gateway: %s | Base: %.2f KZT | Fee: %.2f KZT | Total: %.2f KZT | Auth: %s]",
                transactionId, gatewayName, baseAmountKzt, processingFeeKzt, totalChargedKzt, authorizationDetails
        );
    }
}
